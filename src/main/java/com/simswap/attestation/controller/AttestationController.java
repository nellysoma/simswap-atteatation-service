/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.simswap.attestation.controller;

import com.simswap.attestation.service.AttestationService;
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

    public AttestationController(AttestationService service) {
        this.service = service;
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

    @PostMapping("/verify")
    public Map<String, Object> verify(@RequestBody Map<String, String> body) {

        boolean ok = service.verify(
                body.get("deviceUuid"),
                body.get("challenge"),
                body.get("payload"),
                body.get("signatureBase64")
        );

        return Map.of(
                "decision", ok ? "ALLOW" : "DENY"
        );
    }
}
