package com.niko.train.member.controller.admin;

import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import com.niko.train.member.req.TicketQueryReq;
import com.niko.train.member.resp.TicketQueryResp;
import com.niko.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService ;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req){
        PageResp<TicketQueryResp> pagesResp =  ticketService.queryList(req);
        return new CommonResp<>(pagesResp);
    }
 }