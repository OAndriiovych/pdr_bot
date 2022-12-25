package org.pdr.utils;

import com.liqpay.LiqPay;
import org.json.simple.JSONObject;
import org.pdr.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LiqPayUtil extends LiqPay {
    @Autowired
    MyProperties myProperties2;

    public LiqPayUtil(@Autowired MyProperties myProperties2) {
        super(myProperties2.getLiqPayPublicKey(), myProperties2.getLiqPayPrivateKey());
    }

    public String createUrlForPayment(Payment payment) {
        Map<String, String> params = new HashMap<>();
        params.put("version", "3");
        params.put("action", "pay");
        params.put("amount", "1488");
        params.put("currency", "USD");
        params.put("server_url", myProperties2.getServerUrl());
        params.put("description", "Марічка оплати будь ласка. ну прошу тебе!");
        params.put("order_id", payment.getId() + "1234");
        params.put("sandbox", "1"); // enable the testing environment and card will NOT charged. If not set will be used property isCnbSandbox()

        // code from LiqPay.cnb_form(Map<String, String> params)
        String data = com.liqpay.LiqPayUtil.base64_encode(JSONObject.toJSONString(this.withSandboxParam(this.withBasicApiParams(params))));
        String signature = this.createSignature(data);
        return "https://www.liqpay.ua/api/3/checkout?data=" + data + "&signature=" + signature;
    }
}
