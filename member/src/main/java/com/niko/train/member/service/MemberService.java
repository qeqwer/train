package com.niko.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.niko.train.common.exception.BusinessException;
import com.niko.train.common.exception.BussinessExceptionEnum;
import com.niko.train.common.util.SnowUtil;
import com.niko.train.member.domain.Member;
import com.niko.train.member.domain.MemberExample;
import com.niko.train.member.mapper.MemberMapper;
import com.niko.train.member.req.MemberLoginReq;
import com.niko.train.member.req.MemberRegisterReq;
import com.niko.train.member.req.MemberSendCodeReq;
import com.niko.train.member.resp.MemberLoginResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    
    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
    @Resource
    private MemberMapper memberMapper;
    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq req){
        String mobile = req.getMobile();
        Member memberDB = selectMembers(mobile);

        if (ObjectUtil.isNotNull(memberDB)) {
            // return list.get(0).getId();
            throw new BusinessException(BussinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq req){
        String mobile = req.getMobile();
        Member memberDB = selectMembers(mobile);

        // 如果手机号不存在，则插入
        if (ObjectUtil.isNull(memberDB)) {
            LOG.info("手机号不存在，插入数据库");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            LOG.info("手机存在，不插入数据库");
        }

        // 生成验证码
        // String code = RandomUtil.randomNumbers(4);
        String code = "8888";
        LOG.info("生成短信验证码：{}", code);

        // 保存短信记录表：手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间
        LOG.info("保存短信记录表");

        // 对接短信通道，短信发送
        LOG.info("对接短信通道");

    }

    public MemberLoginResp login(MemberLoginReq req){
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB = selectMembers(mobile);

        // 如果手机号不存在，则抛出异常
        if (ObjectUtil.isNull(memberDB)) {
            throw new BusinessException(BussinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }
        // 校验短信验证码
        if (!code.equals("8888")) {
            throw new BusinessException(BussinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        return BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
    }

    private Member selectMembers(String mobile) {
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(example);
        if (CollUtil.isEmpty(list)) {
            return null;
        }else {
            return list.getFirst();
        }
    }
}
