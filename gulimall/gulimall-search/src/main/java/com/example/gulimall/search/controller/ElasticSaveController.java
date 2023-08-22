package com.example.gulimall.search.controller;

import com.example.common.exception.BizCodeEnume;
import com.example.common.to.es.SkuEsModel;
import com.example.common.utils.R;
import com.example.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {
    @Autowired
    ProductSaveService productSaveService;
    //上架商品
    @PostMapping("/product")
    public R productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        boolean b=false;
        try{
           b= productSaveService.productStatusUp(skuEsModels);
        }catch(Exception e){
            log.error("ElasticSaveController商品上架错误{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if(b){
            return R.ok();
        }
        else{
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
