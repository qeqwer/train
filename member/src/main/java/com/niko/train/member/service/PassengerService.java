package com.niko.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.niko.train.common.util.SnowUtil;
import com.niko.train.member.domain.Passenger;
import com.niko.train.member.mapper.PassengerMapper;
import com.niko.train.member.req.PassengerSaveReq;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {
    
    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);
    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        DateTime now = DateTime.now();
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }




}
