package com.niko.train.business.service;

import com.niko.train.business.domain.DailyTrainSeat;
import com.niko.train.business.domain.DailyTrainTicket;
import com.niko.train.business.mapper.ConfirmOrderMapper;
import com.niko.train.business.mapper.DailyTrainSeatMapper;
import com.niko.train.business.mapper.cust.DailyTrainTicketCustMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AfterConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(AfterConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private DailyTrainTicketCustMapper dailyTrainTicketCustMapper;

    /**
     * 选中座位后的事务处理
     *     座位表修改售卖情况sell；
     *     余票详情表修改余票；
     *     为会员增加购票记录；
     *     更新确认订单为成功。
     */
    @Transactional
    public void afterDoConfirmOrder(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> finalList) {
        for (DailyTrainSeat dailyTrainSeat : finalList) {
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            seatForUpdate.setUpdateTime(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);

            //  A  B  C  D  E  F  G  H  I  J
            //  0  1  2  3  4  5  6  7  8  9
            //   AB BC CD DE EF FG GH HI JI
            //    0  0  1  0  1  1  1  0  1
            // 影响区间[DF] ,[GI]
            // startIndex =4  endIndex = 7,(EF FG GH)
            // minSTART =4-1 = 3(D,111往前碰到最后一个0，即DE)   maxStart = 7-1 = 6(G)
            // minEND =4+1 = 5(F) maxEND = 7+1 = 8(I,111往前碰到最后一个0,即HI)
            Integer startIndex = dailyTrainTicket.getStartIndex();
            Integer endIndex = dailyTrainTicket.getEndIndex();
            char[] chars = seatForUpdate.getSell().toCharArray();

            Integer maxStartIndex = endIndex - 1;
            Integer minEndIndex = startIndex + 1;
            Integer minStartIndex = 0;
            for (int i = startIndex - 1; i >= 0; i--) {
                char aChar = chars[i];
                if (aChar == '1') {
                    minStartIndex = i + 1;
                    break;
                }
            }
            LOG.info("影响出发站区间：" + minStartIndex + "-" + maxStartIndex);


            Integer maxEndIndex = seatForUpdate.getSell().length();
            for (int i = endIndex; i < seatForUpdate.getSell().length(); i++) {
                char aChar = chars[i];
                if (aChar == '1') {
                    maxEndIndex = i;
                    break;
                }
            }
            LOG.info("影响到达站区间：" + minEndIndex + "-" + maxEndIndex);

            dailyTrainTicketCustMapper.updateCountBySell(
                    dailyTrainSeat.getDate(),
                    dailyTrainSeat.getTrainCode(),
                    dailyTrainSeat.getSeatType(),
                    minStartIndex,
                    maxStartIndex,
                    minEndIndex,
                    maxEndIndex
            );


        }
    }

}
