package com.example.gulimall.product.feign;

import com.example.common.to.es.SkuEsModel;
import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(List<SkuEsModel> skuEsModels);
}
