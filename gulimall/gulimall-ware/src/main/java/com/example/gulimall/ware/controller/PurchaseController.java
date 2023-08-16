package com.example.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.gulimall.ware.vo.MergeVo;
import com.example.gulimall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.gulimall.ware.entity.PurchaseEntity;
import com.example.gulimall.ware.service.PurchaseService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 采购信息
 *
 * @author chenlong
 * @email 670233802@qq.com
 * @date 2023-05-08 23:05:03
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    /**
     * 完成采购单
     */
    //ware/purchase/done
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);
        return R.ok();
    }
    /**
     * 领取采购单
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }
    /**
     *ware/purchase/merge
     * {
     *   purchaseId: 1, //整单id
     *   items:[1,2,3,4] //合并项集合
     * }
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }
    /**
     * //ware/purchase/unreceive/list
     */
    @RequestMapping("/unreceive/list")
    public R listUnreceivePurchase(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);
        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setUpdateTime(new Date());
        purchase.setCreateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
