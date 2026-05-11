/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package com.simswap.attestation.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Harmony
 */
@Service
public class OtpService {

    private final Map<String, String> otpStore = new HashMap<>();

    public String generateOtp(String transactionId) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStore.put(transactionId, otp);

        System.out.println("OTP for " + transactionId + ": " + otp); // console demo

        return otp;
    }

    public boolean validateOtp(String transactionId, String otp) {
        String storedOtp = otpStore.get(transactionId);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
