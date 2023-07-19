package com.example.gulimall.member.dao;

import com.example.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author chenlong
 * @email 670233802@qq.com
 * @date 2023-05-08 20:35:17
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
