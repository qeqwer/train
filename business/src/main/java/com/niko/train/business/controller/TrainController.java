package com.niko.train.business.controller;

import com.niko.train.business.resp.TrainQueryResp;
import com.niko.train.business.service.TrainService;
import com.niko.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {

    @Resource
    private TrainService trainService ;

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryAll(){
        List<TrainQueryResp> list =  trainService.queryAll();
        return new CommonResp<>(list);
    }

 }