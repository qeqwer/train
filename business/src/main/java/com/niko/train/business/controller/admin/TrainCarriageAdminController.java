package com.niko.train.business.controller.admin;

import com.niko.train.common.context.LoginMemberContext;
import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import com.niko.train.business.req.TrainCarriageQueryReq;
import com.niko.train.business.req.TrainCarriageSaveReq;
import com.niko.train.business.resp.TrainCarriageQueryResp;
import com.niko.train.business.service.TrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {

    @Resource
    private TrainCarriageService trainCarriageService ;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainCarriageSaveReq req){
        trainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainCarriageQueryResp>> queryList(@Valid TrainCarriageQueryReq req){
        PageResp<TrainCarriageQueryResp> pagesResp =  trainCarriageService.queryList(req);
        return new CommonResp<>(pagesResp);
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        trainCarriageService.delete(id);
        return new CommonResp<>();
    }

 }