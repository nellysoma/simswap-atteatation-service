/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.simswap.attestation.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simswap.attestation.model.Device;
import com.simswap.attestation.repository.DeviceRepository;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.Base64;
import java.util.Map;

/**
 *
 * @author Harmony
 */
@RestController
@RequestMapping("/api/v1/testD")
public class TestSignatureSimulatorController {

    private final DeviceRepository deviceRepository;

    public TestSignatureSimulatorController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
    
     @PostMapping("/simulate-signature")
    public Map<String, String> simulateSignature(
            @RequestBody Map<String, String> body
    ) throws Exception {

        String deviceUuid = body.get("deviceUuid");
        String challenge = body.get("challenge");
        String payload = body.get("payload");

        // Generate temporary RSA key pair
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Save public key so /verify can use it
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        Device device = new Device();
        device.setDeviceUuid(deviceUuid);
        //device.setPublicKey(publicKeyBase64);

        deviceRepository.save(device);

        // Sign challenge + payload
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        signature.update(challenge.getBytes("UTF-8"));
        signature.update(payload.getBytes("UTF-8"));

        byte[] signedBytes = signature.sign();

        String signatureBase64 = Base64.getEncoder().encodeToString(signedBytes);

        return Map.of(
                "deviceUuid", deviceUuid,
                "publicKeyBase64", publicKeyBase64,
                "signatureBase64", signatureBase64
        );
    }
}
