package com.example.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
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
	@NotEmpty
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
	@NotBlank
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty
	@URL(message="logo必须是一个合法的url地址")
	private String logo;
	/**
	 * 介绍
	 */
	@NotEmpty
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotEmpty
	private Integer showStatus;
	/**
	 * 检索首字母
	 * 自定义规则没有相应注解，但可以使用自定义Pattern
	 */
	@NotEmpty
	@Pattern(regexp = "/^[a-zA-Z]$/",message = "检索首字母必须是一个字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotEmpty
	@Min(value = 0)
	private Integer sort;

}
