package com.niko.train.member.req;

import com.niko.train.common.req.PageReq;

public class TicketQueryReq extends PageReq {

    private  Long MemberId;

    public Long getMemberId() {
        return MemberId;
    }

    public void setMemberId(Long memberId) {
        MemberId = memberId;
    }

    @Override
    public String toString() {
        return "TicketQueryReq{" +
                "MemberId=" + MemberId +
                "} " + super.toString();
    }
}