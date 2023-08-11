package com.example.gulimall.product.service.impl;

import com.example.common.to.SkuReductionTo;
import com.example.common.to.SpuBoundTo;
import com.example.common.utils.R;
import com.example.gulimall.product.dao.SpuInfoDescDao;
import com.example.gulimall.product.entity.*;
import com.example.gulimall.product.feign.CouponFeignService;
import com.example.gulimall.product.service.*;
import com.example.gulimall.product.vo.*;
import com.mysql.cj.util.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    AttrService attrService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }
    /**
     * TODO 在高级部分展开完善

     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
          //1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        //2.保存spu的描述图片  pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        //图片的链接用逗号分割
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //3.保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);
        //4.保存spu的规格参数；pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity byId = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(byId.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);
        //5.保存spu的积分信息 gulimall_sms_spu_bounds
        
        //5.保存当前spu对应的所有sku信息

        List<Skus> skus = vo.getSkus();
        //5.1 sku的基本信息 pms_sku_info
        if(skus!=null&&skus.size()>0)
        skus.forEach(item->{
            String defaultImg="";
            for(Images image:item.getImages()){
                if(image.getDefaultImg()==1){
                    defaultImg=image.getImgUrl();
                }
            }
            //  private String skuName;
            //    private BigDecimal price;
            //    private String skuTitle;
            //    private String skuSubtitle;
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(item,skuInfoEntity);
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setSkuDefaultImg(defaultImg);
            skuInfoService.saveSkuInfo(skuInfoEntity);
            Long skuId=skuInfoEntity.getSkuId();
            //5.2 sku的图片信息 pms_sku_images
            //TODO 没有图片路径的无需保存
            //所以需要对这个collector做过滤
            List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                return skuImagesEntity;
            }).filter(entity->{
                //返回true就是需要，false就是剔除
                return !StringUtils.isNullOrEmpty(entity.getImgUrl());
            }).collect(Collectors.toList());
            skuImagesService.saveBatch(imagesEntities);
            //5.3 sku的销售属性信息 pms_sku_sale_attr_value
            List<Attr> attr = item.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
            //以下需要调用远程服务才能完成
            Bounds bounds = vo.getBounds();
            //A服务给B服务发数据，可以将这些数据封装为一个对象，在A发送该对象数据时，会将其默认封装为一个json
            //然后B接收到json文件，解析为一个对象
            //传输期间的数据模型成为TO，由于A和B都要使用，可以将该类放到common中
            SpuBoundTo spuBoundTo = new SpuBoundTo();
            BeanUtils.copyProperties(bounds,spuBoundTo);
            spuBoundTo.setSpuId(spuInfoEntity.getId());
            R r= couponFeignService.saveSpuBounds(spuBoundTo);
            if(r.getCode()!=0){
                log.error("远程保存spu积分信息失败");
            }
            //5.4 sku的优惠、满减等信息；gulimall_sms跨数据库，sms_sku_ladder\sms_sku_full_reduction
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item,skuReductionTo);
            skuReductionTo.setSkuId(skuId);
            //由于skuReductionTo.getFullPrice()是bigdecimel类型，不能直接比较，需要用compareto方法
            if(skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                R r1=couponFeignService.saveSkuReduction(skuReductionTo);
                if(r1.getCode()!=0){
                    log.error("远程保存spu积分信息失败");
                }
            }
        });

     }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper=new QueryWrapper<>();
        String key=(String) params.get("key");
        if(!StringUtils.isNullOrEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if(!StringUtils.isNullOrEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId=(String) params.get("brandId");
        if(!StringUtils.isNullOrEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId=(String) params.get("catelogId");
        if(!StringUtils.isNullOrEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        /** catelogId: 6,//三级分类id
         brandId: 1,//品牌id
         status: 0,//商品状态
         */
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }


}