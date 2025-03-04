package com.niko.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niko.train.business.domain.Train;
import com.niko.train.business.domain.TrainExample;
import com.niko.train.business.mapper.TrainMapper;
import com.niko.train.business.req.TrainQueryReq;
import com.niko.train.business.req.TrainSaveReq;
import com.niko.train.business.resp.TrainQueryResp;
import com.niko.train.common.exception.BusinessException;
import com.niko.train.common.exception.BussinessExceptionEnum;
import com.niko.train.common.resp.PageResp;
import com.niko.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);
    @Resource
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req) {
        Train train = BeanUtil.copyProperties(req, Train.class);
        DateTime now = DateTime.now();
        if(ObjectUtil.isNull(req.getId())){
            // 唯一校验
            Train trainDB = selectByUnique(train.getCode());
            if(ObjectUtil.isNotNull(trainDB)){
                throw new BusinessException(BussinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    private Train selectByUnique(String trainCode) {
        // 保存之前，校验唯一性
        TrainExample trainCarriageExample = new TrainExample();
        TrainExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andCodeEqualTo(trainCode);
        List<Train> list = trainMapper.selectByExample(trainCarriageExample);
        if(CollUtil.isNotEmpty(list)){
            return list.get(0);
        } else {
            return null;
        }
    }

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        TrainExample trainExample = new TrainExample();
        // 降序
        trainExample.setOrderByClause("id desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Train> trainList = trainMapper.selectByExample(trainExample);

        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);

        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;

    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    public List<TrainQueryResp> queryAll( ) {
        List<Train> trainList = selectAll();
        return BeanUtil.copyToList(trainList, TrainQueryResp.class);

    }

    public List<Train> selectAll() {
        TrainExample trainExample = new TrainExample();
        // 降序
        trainExample.setOrderByClause("code asc");
        return trainMapper.selectByExample(trainExample);
    }


}
