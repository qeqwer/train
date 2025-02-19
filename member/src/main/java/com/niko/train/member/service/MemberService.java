package com.niko.train.member.service;

import cn.hutool.core.collection.CollUtil;
import com.niko.train.common.exception.BusinessException;
import com.niko.train.common.exception.BussinessExceptionEnum;
import com.niko.train.member.domain.Member;
import com.niko.train.member.domain.MemberExample;
import com.niko.train.member.mapper.MemberMapper;
import com.niko.train.member.req.MemberRegisterReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;
    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq req){
        String mobile = req.getMobile();
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(example);

        if (CollUtil.isNotEmpty(list)) {
            // return list.get(0).getId();
            throw new BusinessException(BussinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
