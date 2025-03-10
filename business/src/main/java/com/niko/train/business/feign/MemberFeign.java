package com.niko.train.business.feign;

import com.niko.train.common.req.MemberTicketReq;
import com.niko.train.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member", url = "http://127.0.0.1:8001/member")
public interface MemberFeign {

    @GetMapping("/feign/ticket/save")
    CommonResp<Object> save(@Valid @RequestBody MemberTicketReq req);


}
