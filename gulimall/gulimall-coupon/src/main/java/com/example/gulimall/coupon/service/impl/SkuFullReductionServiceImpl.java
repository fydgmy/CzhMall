package com.example.gulimall.coupon.service.impl;

import com.example.common.to.MemberPrice;
import com.example.common.to.SkuReductionTo;
import com.example.gulimall.coupon.entity.MemberPriceEntity;
import com.example.gulimall.coupon.entity.SkuLadderEntity;
import com.example.gulimall.coupon.service.MemberPriceService;
import com.example.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.coupon.dao.SkuFullReductionDao;
import com.example.gulimall.coupon.entity.SkuFullReductionEntity;
import com.example.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    SkuLadderService skuLadderService;
    @Autowired
    MemberPriceService memberPriceService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //1.保存满减打折，会员价
        //5.4 sku的优惠、满减等信息；gulimall_sms跨数据库，sms_sku_ladder\sms_sku_full_reduction
        //sku_ladder:id	sku_id	full_count	discount	price	add_other
        //sku_full_reduction:id	sku_id	full_price	reduce_price	add_other
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if(skuReductionTo.getFullCount()>0){
            //skuLadderEntity.setPrice();
            skuLadderService.save(skuLadderEntity);
        }
        //2.sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity=new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,skuFullReductionEntity);
        if(skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.save(skuFullReductionEntity);
        }
        //3.sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter(
                item->{
                    return item.getMemberPrice().compareTo(new BigDecimal("0"))==1;
                }
        ).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }

}