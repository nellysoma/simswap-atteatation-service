/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.simswap.attestation.controller;

import com.simswap.attestation.model.Decision;
import static com.simswap.attestation.model.Decision.HIGH_RISK;
import com.simswap.attestation.service.AttestationService;
import com.simswap.attestation.service.FraudDecisionEngine;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Harmony
 */
@RestController
@RequestMapping("/api/v1/attest")
public class AttestationController {

    private final AttestationService service;
    private final FraudDecisionEngine fraudDecisionEngine;
    

    public AttestationController(AttestationService service,FraudDecisionEngine fraudDecisionEngine) {
        this.service = service;
         this.fraudDecisionEngine = fraudDecisionEngine;
    }

    /**@PostMapping("/enroll")
    public Map<String, Object> enroll(@RequestBody Map<String, String> body) {

        service.enroll(
                body.get("deviceUuid"),
                body.get("publicKeyBase64")
        );

        return Map.of("success", true);
    }**/

    @GetMapping("/challenge")
    public Map<String, Object> challenge(@RequestParam String deviceUuid) {

        String ch = service.generateChallenge(deviceUuid);

        return Map.of("challenge", ch);
    }

   /*** @PostMapping("/verify")
    public Map<String, Object> verify(@RequestBody Map<String, String> body) {

        boolean ok = service.verify(
                body.get("deviceUuid"),
                body.get("challenge"),
                body.get("payload"),
                body.get("signatureBase64")
        );

        return Map.of("decision", ok ? "ALLOW" : "DENY");
    }**/
    
    // ✅ 3. Verify + Fraud Decision
    @PostMapping("/verify")
    public Map<String, Object> verify(@RequestBody Map<String, String> body) {

        String deviceUuid = body.get("deviceUuid");
        String challenge = body.get("challenge");
        String payload = body.get("payload");
        String signature = body.get("signatureBase64");

        // 🔐 STEP 1: Cryptographic Attestation
        boolean isValid = service.verify(
                deviceUuid,
                challenge,
                payload,
                signature
        );

        // ❌ If signature invalid → immediate deny
        if (!isValid) {
            return Map.of(
                    "decision", "DENY",
                    "reason", "Invalid device signature"
            );
        }

        // 🧠 STEP 2: Fraud Detection
        Decision decision =
                fraudDecisionEngine.evaluate(deviceUuid, payload);

        
        /**if (null == decision){
        return Map.of(
        "decision", decision.name(),
        "reason", "This device is heavily compromised and as such access is denied"
        );
        }else**/
        //return Map.of("decision", decision.name());
        return switch (decision) {
            case ALLOW -> Map.of(
                    "decision", decision.name(),
                    "reason", "Verified device"
            );
            case CHALLENGE -> Map.of(
                    "decision", decision.name(),
                    "reason", "The SIM card of this device has changed please carryout the Challenge to prove you are the owner of this phone"
            );
            case HIGH_RISK -> Map.of(
                    "decision", decision.name(),
                    "reason", "This device is heavily compromised, the City and IP Address does not match those registered, also the device seems to be rooted or the request might be coming from an Emulator and as such access is denied"
            );
            case DENY -> Map.of(
                    "decision", decision.name(),
                    "reason", "Invalid device signature in switch case"
            );
        };
        
    }
}
