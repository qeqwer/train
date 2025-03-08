package com.niko.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niko.train.business.domain.*;
import com.niko.train.business.enums.ConfirmOrderStatusEnum;
import com.niko.train.business.enums.SeatColEnum;
import com.niko.train.business.enums.SeatTypeEnum;
import com.niko.train.business.mapper.ConfirmOrderMapper;
import com.niko.train.business.req.ConfirmOrderDoReq;
import com.niko.train.business.req.ConfirmOrderQueryReq;
import com.niko.train.business.req.ConfirmOrderTicketReq;
import com.niko.train.business.resp.ConfirmOrderQueryResp;
import com.niko.train.common.context.LoginMemberContext;
import com.niko.train.common.exception.BusinessException;
import com.niko.train.common.exception.BussinessExceptionEnum;
import com.niko.train.common.resp.PageResp;
import com.niko.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);
    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    public void save(ConfirmOrderDoReq req) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        DateTime now = DateTime.now();
        if(ObjectUtil.isNull(confirmOrder.getId())){
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        // 降序
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        PageHelper.startPage(req.getPage(),req.getSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);

        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    public void doConfirm(ConfirmOrderDoReq req) {
        // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否再有效期内，tickets条数>0，同乘客同车次是否已买过票


        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        List<ConfirmOrderTicketReq> ticketList = req.getTickets();

        // 保存确认订单，状态初始
        DateTime now = DateTime.now();

        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setMemberId(LoginMemberContext.getId());
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setTickets(JSON.toJSONString(ticketList));

        confirmOrderMapper.insert(confirmOrder);

        // 查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(date, trainCode, start, end);
        LOG.info("查出余票记录：{}", dailyTrainTicket);

        //扣减余票数量，并判断余票是否足够
        reduceTickets(ticketList, dailyTrainTicket);

        // 计算相对第一个座位的偏离值
        // 比如选择的是c1、d2，则偏移量为：[0, 5]
        // 比如选择的是a1、b1、c1，则偏移量为：[0, 1, 2]
        ConfirmOrderTicketReq ticketReq0 = ticketList.get(0); //获取第一张票，判断座位是否为空（是否选座）
        if (StrUtil.isNotBlank(ticketReq0.getSeat())){
            LOG.info("本次购票有选座");
            // 查出本次选座的座位类型有哪些列，用于计算所选座位与第一个座位的偏
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            LOG.info("本次选座的作为类型包含的列");

            // 组成和前端两拍选座一样的列表，用于作参照的座位列表。例如：referSeatList = {A1, C1, D1, F1, A2, C2, D2, F2}
            ArrayList<Object> referSeatList = new ArrayList<>();
            for(int i = 1; i <= 2; i++){
                for (SeatColEnum seatColEnum : colEnumList){
                    referSeatList.add(seatColEnum.getCode() + i);
                }
            }
            LOG.info("用于参照的两排座位:{}", referSeatList);

            // 绝对偏移值，即：在操作座位列表中的位置
            List<Integer> aboluteOffsetList = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : ticketList){
                int index = referSeatList.indexOf(ticketReq.getSeat());
                aboluteOffsetList.add(index);
            }
            LOG.info("计算得到所有座位的绝对偏移值：{}", aboluteOffsetList);

            // 相对偏移值，即：在操作座位列表中的位置减去第一个座位的位置
            List<Integer> offsetList = new ArrayList<>();
            for(Integer index: aboluteOffsetList){
                int offset = index - aboluteOffsetList.get(0);
                offsetList.add(offset);
            }
            LOG.info("计算得到所有座位的相对第一个座位移值：{}", offsetList);

            getSeat(date,
                    trainCode,
                    ticketReq0.getSeatTypeCode(),
                    ticketReq0.getSeat().split("")[0], //A1 -> A
                    offsetList,
                    dailyTrainTicket.getStartIndex(),
                    dailyTrainTicket.getEndIndex()
            );

        } else {
            LOG.info("本次购票无选座");
            for(ConfirmOrderTicketReq ticketReq : ticketList){
                getSeat(date,
                        trainCode,
                        ticketReq0.getSeatTypeCode(),
                        null,
                        null,dailyTrainTicket.getStartIndex(),
                        dailyTrainTicket.getEndIndex()
                );
            }
        }

        //选座


    }

    /**
     * 挑座位，如果有选座，则一次性挑完，如果无选座，则一个一个挑
     * @param date
     * @param trainCode
     * @param seatType
     * @param column
     * @param offsetList
     */
    private void getSeat(Date date, String trainCode, String seatType, String column,
                         List<Integer> offsetList, Integer startIndex, Integer endIndex){
        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);
        LOG.info("查出{}个符合条件的车厢", carriageList.size());

        // 一个车厢一个车厢的获取座位
        for(DailyTrainCarriage dailyTrainCarriage : carriageList){
            LOG.info("开始从车厢{}选座", dailyTrainCarriage.getIndex());
            List<DailyTrainSeat> seatList = dailyTrainSeatService
                    .selectByCarriage(date, trainCode, seatType, dailyTrainCarriage.getIndex());
            LOG.info("车厢{}的座位数是：{}个", dailyTrainCarriage.getIndex(), seatList.size());
            for(DailyTrainSeat dailyTrainSeat : seatList){
                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if(isChoose){
                    LOG.info("选中座位");
                    return;
                } else {
                    LOG.info("未选中座位");
                    continue;
                }
            }
        }
    }

    /**
     * 计算某座位在区间内是否可卖
     * 例子：sell = 10001，本次购买区间站 1~4, 则区间已售 000
     * 全都是0，表示这个区间可买，只要有1，就表示区间内已售过票
     *
     * 选中后，要计算购票后的sell，比如原来是10001，本次购买区间为 1~4 站
     * 方案：构造造本次购票造成的售卖信息01110，和原sell 10001 按位与。最终得到111111
     */
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex){
        String sell = dailyTrainSeat.getSell();
        String sellPart = sell.substring(startIndex, endIndex);
        if(Integer.parseInt(sellPart) > 0){
            LOG.info("座位{}在本次车站区间{}~{}已销售，不可选中座位", dailyTrainSeat
                    .getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            LOG.info("座位{}在本次车站区间{}~{}未销售，可正常选票", dailyTrainSeat
                    .getCarriageSeatIndex(), startIndex, endIndex);
            //  111
            String curSell = sellPart.replace('0', '1');
            // 0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
            // 01110
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

            // 当前区间售票信息与库里的已售信息按位与，即可得到该座位卖出此票后的售票情况
            // 32
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);
            // 11111
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());

            LOG.info("座位{}被选中，原售票信息：{}，车站区间：{}~{}，即：{}，最终售票信息：{}"
                    , dailyTrainSeat.getCarriageSeatIndex(),
                    sell, startIndex, endIndex, curSell, newSell);
            dailyTrainSeat.setSell(newSell);
            return true;
        }

    }



    private static void reduceTickets(List<ConfirmOrderTicketReq> ticketList, DailyTrainTicket dailyTrainTicket) {
        for(ConfirmOrderTicketReq ticketReq : ticketList){
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
            switch (seatTypeEnum){
                case YDZ -> {
                    int countLeft = dailyTrainTicket.getYdz() - 1;
                    if(countLeft < 0){
                        throw new BusinessException(BussinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(countLeft);
                }
                case EDZ -> {
                    int countLeft = dailyTrainTicket.getEdz() - 1;
                    if(countLeft < 0){
                        throw new BusinessException(BussinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setEdz(countLeft);
                }
                case RW -> {
                    int countLeft = dailyTrainTicket.getRw() - 1;
                    if(countLeft < 0){
                        throw new BusinessException(BussinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setRw(countLeft);
                }
                case YW -> {
                    int countLeft = dailyTrainTicket.getYw() - 1;
                    if(countLeft < 0){
                        throw new BusinessException(BussinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYw(countLeft);
                }
            }
        }
    }


}
