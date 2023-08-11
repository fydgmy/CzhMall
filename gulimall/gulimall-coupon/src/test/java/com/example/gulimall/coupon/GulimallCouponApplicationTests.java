package com.example.gulimall.coupon;

import com.example.gulimall.coupon.entity.CouponEntity;
import com.example.gulimall.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallCouponApplicationTests {
    @Autowired
    CouponService couponService;
    @Test
    void contextLoads() {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("awadw");
        couponService.save(couponEntity);
        System.out.println("保存成功！");
    }

}
