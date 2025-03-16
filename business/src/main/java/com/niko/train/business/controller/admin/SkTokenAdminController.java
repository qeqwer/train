package com.niko.train.business.controller.admin;

import com.niko.train.common.context.LoginMemberContext;
import com.niko.train.common.resp.CommonResp;
import com.niko.train.common.resp.PageResp;
import com.niko.train.business.req.SkTokenQueryReq;
import com.niko.train.business.req.SkTokenSaveReq;
import com.niko.train.business.resp.SkTokenQueryResp;
import com.niko.train.business.service.SkTokenService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/sk-token")
public class SkTokenAdminController {

    @Resource
    private SkTokenService skTokenService ;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody SkTokenSaveReq req){
        skTokenService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<SkTokenQueryResp>> queryList(@Valid SkTokenQueryReq req){
        PageResp<SkTokenQueryResp> pagesResp =  skTokenService.queryList(req);
        return new CommonResp<>(pagesResp);
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        skTokenService.delete(id);
        return new CommonResp<>();
    }

 }