package com.example.gulimall.product.service.impl;

import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.product.dao.SkuInfoDao;
import com.example.gulimall.product.entity.SkuInfoEntity;
import com.example.gulimall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        /**
         * key: '华为',//检索关键字
         * catelogId: 0,
         * brandId: 0,
         * min: 0,
         * max: 0
         */
        String key =(String)params.get("key");
        if(!StringUtils.isNullOrEmpty(key)){
            wrapper.and(w->{
               w.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        String catelogId =(String)params.get("catelogId");
        if(!StringUtils.isNullOrEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){

            wrapper.eq("catalog_id",catelogId);
        }
        String brandId =(String)params.get("brandId");
        if(!StringUtils.isNullOrEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String min =(String)params.get("min");
        if(!StringUtils.isNullOrEmpty(min)){
            wrapper.ge("price",min);
        }
        String max =(String)params.get("max");
        if(!StringUtils.isNullOrEmpty(max)){
            try{
                BigDecimal max1 = new BigDecimal(max);
                if(max1.compareTo(new BigDecimal("0"))==1){
                    wrapper.le("price",max);
                }
            }catch (Exception e){

            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return null;
    }

}