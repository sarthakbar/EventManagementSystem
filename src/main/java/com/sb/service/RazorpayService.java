package com.sb.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public Order createOrder(double amount) {
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject options = new JSONObject();
            options.put("amount", (int)(amount * 100)); // MUST be int
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());

            return client.orders.create(options);

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order", e);
        }
    }
}
