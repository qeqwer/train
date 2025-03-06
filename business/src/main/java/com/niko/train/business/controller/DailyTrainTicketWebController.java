package com.niko.train.business.controller;

import com.niko.train.business.req.DailyTrainTicketQueryReq;
import com.niko.train.business.resp.DailyTrainTicketQueryResp;
import com.niko.train.business.service.DailyTrainTicketService;
import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/daily-train-ticket")
public class DailyTrainTicketWebController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService ;

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req){
        PageResp<DailyTrainTicketQueryResp> pagesResp =  dailyTrainTicketService.queryList(req);
        return new CommonResp<>(pagesResp);
    }


 }