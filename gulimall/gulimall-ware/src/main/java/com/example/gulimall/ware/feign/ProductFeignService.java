package com.example.gulimall.ware.feign;

import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {
    /**
     *    /product/skuinfo/info/{skuid}
     *    给商品服务所在的机器发送请求
     *    /api/product/skuinfo/info/{skuid}
     *    如果带了api，就可以修改product为gateway，即向网关发送请求
     *    1）要让所有请求过网关
     *    @FeignClient("gulimall-gateway")，给gulimall-gateway所在机器发请求
     *    2/api/product/skuinfo/info/{skuid}
     *    2）直接让后台指定服务处理
     *    @FeignClient("gulimall-product")
     *    /product/skuinfo/info/{skuid}
     */
    @RequestMapping("product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
