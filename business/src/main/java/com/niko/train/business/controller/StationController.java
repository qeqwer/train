package com.niko.train.business.controller;

import com.niko.train.business.resp.StationQueryResp;
import com.niko.train.business.service.StationService;
import com.niko.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {

    @Resource
    private StationService stationService ;

    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryAll(){
        List<StationQueryResp> list =  stationService.queryAll();
        return new CommonResp<>(list);
    }

 }