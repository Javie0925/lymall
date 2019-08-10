package com.leyou.common.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnmu {

    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),  //必须在最前面
    CATEGORY_NOT_FOUND(404,"商品分类没查到"),
    BRAND_NOT_FOUND(404,"品牌不存在"),
    SPU_NOT_FOUND(404,"SPU不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    FILE_UPLOAD_FAIL(500,"文件上传失败"),
    INVALIDE_FILE_TYPE(500,"无效的文件类型"),
    SPECIFICATIONS_INSERT_FAIL(500,"规格添加失败"),
    INTERNAL_SAVE_ERROR(500,"添加失败"),
    USER_DATA_TYPE_ERROR(400,"无效的数据类型"),   //badrequest
    INVALID_VERIFY_CODE(400,"无效的验证码"),
    INVALID_PASSWORD(400,"密码错误"),
    INVALID_USERNAME(400,"用户名不存在"),
    INVALID_USERNAME_PASSWORD(400,"用户名或密码有误"),
    TOKEN_CREAT_ERROR(500,"用户凭证生成失败"),
    UN_AUTHORIZED(403,"未授权"),
    CART_NOT_FOUND(404,"未找到该商品"),
    EMPTY_USER_ADDRESS(404,"用户地址列表为空"),
    ORDER_CREATED_ERROR(500,"订单创建失败"),
    WXPAY_PAY_CODE_CREATED_ERROR(500,"微信下单码创建失败"),
    ORDER_NOT_FOUND(404,"订单未找到"),
    ORDER_STATUS_ERROR(500,"订单状态异常"),
    INVALID_SIGN(400,"无效的签名"),
    INVALID_ORDER_PARAM(400,"订单参数异常"),
    UPDATE_ORDER_STATUS_ERROR(500,"订单状态修改失败"),
    ;
    private int code;
    private String msg;
}
