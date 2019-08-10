package com.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

/**
 * @author javie
 * @date 2019/6/1 21:19
 */
@Data
public class PayConfig implements WXPayConfig {

    private String appID; // 公众账号id

    private String mchID; // 商户号

    private String key; //  生成签名的密钥

    private int httpConnectTimeoutMs;  // 连接超时时间

    private int httpReadTimeoutMs; // 读取超时时长

    private String notifyUrl; // 下单通知回调地址

    @Override
    public InputStream getCertStream() {
        return null;
    }
}
