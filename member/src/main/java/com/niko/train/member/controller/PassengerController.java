package com.niko.train.member.controller;

import com.niko.train.common.context.LoginMemberContext;
import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import com.niko.train.member.req.PassengerQueryReq;
import com.niko.train.member.req.PassengerSaveReq;
import com.niko.train.member.resp.PassengerQueryResp;
import com.niko.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService ;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req){
        passengerService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req){
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> pagesResp =  passengerService.queryList(req);
        return new CommonResp<>(pagesResp);
    }

}
