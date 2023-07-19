package com.example.gulimall.order.dao;

import com.example.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author chenlong
 * @email 670233802@qq.com
 * @date 2023-05-08 22:58:39
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
