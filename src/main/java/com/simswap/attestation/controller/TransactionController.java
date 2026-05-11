/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.simswap.attestation.controller;

import com.simswap.attestation.model.Decision;
import com.simswap.attestation.service.AttestationService;
import com.simswap.attestation.service.FraudDecisionEngine;
import com.simswap.attestation.service.FraudDecisionResult;
import com.simswap.attestation.service.OtpService;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Harmony
 */
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final AttestationService attestationService;
    private final FraudDecisionEngine fraudDecisionEngine;
    private final OtpService otpService;

    public TransactionController(
            AttestationService attestationService,
            FraudDecisionEngine fraudDecisionEngine,
            OtpService otpService
    ) {
        this.attestationService = attestationService;
        this.fraudDecisionEngine = fraudDecisionEngine;
        this.otpService = otpService;
    }

    @PostMapping("/process")
    public Map<String, Object> processTransaction(
            @RequestBody Map<String, Object> body
    ) {

        String deviceUuid = (String) body.get("deviceUuid");
        String challenge = (String) body.get("challenge");
        String payload = (String) body.get("payload");
        String signature = (String) body.get("signatureBase64");

        // Transaction details (for demo purposes)
        Object amount = body.get("amount");
        Object recipient = body.get("recipient");

        // 🔐 STEP 1: Device Attestation
        boolean isValid = attestationService.verify(
                deviceUuid,
                challenge,
                payload,
                signature
        );

        if (!isValid) {
            return Map.of(
                    "status", "FAILED",
                    "decision", "DENY",
                    "reason", "Invalid device signature",
                    "riskScore", 100,
                    "amount", amount,
                    "recipient", recipient
            );
        }

        // 🧠 STEP 2: Fraud Evaluation
        Decision decision
                = fraudDecisionEngine.evaluate(deviceUuid, payload);

        String status;

        switch (decision) {
            case ALLOW:
                status = "SUCCESS";
                break;

            case CHALLENGE:
                String txnId = otpService.generateTransactionId();
                otpService.generateOtp(txnId);
                status = "PENDING";
                break;

            case DENY:
            default:
                status = "FAILED";
                break;
        }

        return Map.of(
                "status", status,
                /**
                 * "decision", result.getDecision().name(), "reason",
                 * result.getReason(), "riskScore", result.getRiskScore(),*
                 */
                "amount", amount,
                "recipient", recipient
        );
    }

    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody Map<String, String> body) {

        String transactionId = body.get("transactionId");
        String otp = body.get("otp");

        boolean valid = otpService.validateOtp(transactionId, otp);

        if (!valid) {
            return Map.of(
                    "status", "FAILED",
                    "message", "Invalid OTP"
            );
        }

        return Map.of(
                "status", "SUCCESS",
                "message", "Transaction completed after OTP verification"
        );
    }
}
