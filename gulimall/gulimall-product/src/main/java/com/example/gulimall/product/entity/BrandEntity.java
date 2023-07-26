package com.example.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.example.common.validator.ListValue;
import com.example.common.validator.group.AddGroup;
import com.example.common.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author chenlong
 * @email 670233802@qq.com
 * @date 2023-05-08 19:47:18
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	//group中指定一个接口类型
	@Null(message = "新增不能指定Id",groups ={AddGroup.class} )
	@NotNull(message = "修改必须指定品牌Id",groups ={UpdateGroup.class} )
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 * @NotNull 不能为空null
	 * @NotEmpty 不为null或不为空串
	 * @NotBlank 必须包含至少一个非空字符 不能为一个空格
	 *
	 */
	@NotEmpty
	@NotBlank(message = "品牌名必须提交",groups={AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	//@NotEmpty(groups = {AddGroup.class})
	@URL(message="logo必须是一个合法的url地址",groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	@NotEmpty
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups={UpdateGroup.class})
	@ListValue(vals={0,1},groups={AddGroup.class, UpdateGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 * 自定义规则没有相应注解，但可以使用自定义Pattern
	 */
	@NotEmpty(groups={AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须是一个字母",groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotEmpty
	@Min(value = 0)
	private Integer sort;

}
