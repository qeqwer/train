package com.niko.train.business.controller.admin;

import com.niko.train.business.req.DailyTrainQueryReq;
import com.niko.train.business.req.DailyTrainSaveReq;
import com.niko.train.business.resp.DailyTrainQueryResp;
import com.niko.train.business.service.DailyTrainService;
import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {

    @Resource
    private DailyTrainService dailyTrainService ;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainSaveReq req){
        dailyTrainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainQueryResp>> queryList(@Valid DailyTrainQueryReq req){
        PageResp<DailyTrainQueryResp> pagesResp =  dailyTrainService.queryList(req);
        return new CommonResp<>(pagesResp);
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        dailyTrainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/gen-daily/{date}")
    public CommonResp<Object> genDaily(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date date){
        dailyTrainService.genDaily(date);
        return new CommonResp<>();
    }

 }