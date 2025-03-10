package com.niko.train.business.controller.admin;

import com.niko.train.business.req.TrainQueryReq;
import com.niko.train.business.req.TrainSaveReq;
import com.niko.train.business.resp.TrainQueryResp;
import com.niko.train.business.service.TrainSeatService;
import com.niko.train.business.service.TrainService;
import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {

    @Resource
    private TrainService trainService ;
    @Resource
    private TrainSeatService seatService ;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSaveReq req){
        trainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req){
        PageResp<TrainQueryResp> pagesResp =  trainService.queryList(req);
        return new CommonResp<>(pagesResp);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        trainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryAll(){
        List<TrainQueryResp> list =  trainService.queryAll();
        return new CommonResp<>(list);
    }

    @GetMapping("/gen-seat/{trainCode}")
    public CommonResp<Object> genSeat(@PathVariable String trainCode){
        seatService.genTrainSeat(trainCode);
        return new CommonResp<>();
    }


 }