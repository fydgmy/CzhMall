package com.example.gulimall.product.dao;

import com.example.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author chenlong
 * @email 670233802@qq.com
 * @date 2023-05-08 19:47:18
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
