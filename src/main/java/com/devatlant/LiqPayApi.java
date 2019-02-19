package com.devatlant;

import com.devatlant.model.LiqPayContract;

import java.util.Map;

public interface LiqPayApi {
    String API_VERSION = "3";
    String LIQPAY_API_URL = "https://www.liqpay.ua/api/";
    String LIQPAY_API_CHECKOUT_URL = "https://www.liqpay.ua/api/3/checkout";
    String DEFAULT_LANG = "ru";

    Map<String, Object> api(String path, Map<String, Object> params) throws Exception;

    /**
     * Liq and Buy
     * Payment acceptance on the site client to server
     * To accept payments on your site you will need:
     * Register on www.liqpay.ua
     * Create a store in your account using install master
     * Get a ready HTML-button or create a simple HTML form
     * HTML form should be sent by POST to URL https://www.liqpay.ua/api/3/checkout Two parameters data and signature, where:
     * data - function result base64_encode( $json_string )
     * signature - function result base64_encode( sha1( $private_key . $data . $private_key ) )
     */
    String cnb_form(Map<String, Object> params);

    /**
     * generate data and signature in specified LiqPay format
     * @param params all payments params to be used
     * @return data and signature
     */
    LiqPayContract generateApiContract(Map<String, Object> params);
    String createSignature(String dataToSign);
}
