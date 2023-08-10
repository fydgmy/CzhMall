package com.example.gulimall.product.feign;

import com.example.common.to.SkuReductionTo;
import com.example.common.to.SpuBoundTo;
import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    //这里要加注解@RequestBody 相当于要和远程的接口保证完整的签名
    //但是如果在feign远程调用时SpuBoundTo spuBoundTo，在那边为spuBoundEntity是否可行？
    /**
     * 1.如果有一个service调用了CouponFeignService的如下方法，并且传入一个对象SpuBoundTo spuBoundTo
     * 那么springcloud将做如下步骤：
     * 1.1@RequestBody将这个对象转为json
     * 1.2springcloud会从注册中心找到该远程服务，给/coupon/spubounds/save发送请求
     * 将上一步转的json放在请求体位置，发送请求
     * 1.3对方服务收到请求，请求体里有json数据
     * 1.4对方服务的@RequestBody SpuBoundsEntity spuBounds，将请求体的json转为该类型
     * 理论上可以转，只要前面的对象属性名和该接收对象的属性名一一对应
     * 只要json的数据模型是兼容的，双方服务无需使用同一个to
     */
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);
    @PostMapping("coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
