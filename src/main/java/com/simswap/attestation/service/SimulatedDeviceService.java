/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */

package com.simswap.attestation.service;

import org.springframework.stereotype.Service;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

import com.simswap.attestation.repository.DeviceRepository;
import com.simswap.attestation.model.Device;

/**
 *
 * @author Harmony
 */
@Service
public class SimulatedDeviceService {

    private final Map<String, PrivateKey> privateKeyStore = new HashMap<>();
    private final Map<String, PublicKey> publicKeyStore = new HashMap<>();
    
    private final DeviceRepository deviceRepo;
    
    public SimulatedDeviceService(DeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }
    
    
    public String enrollDevice(String deviceUuid) throws Exception {
        
        
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        privateKeyStore.put(deviceUuid, keyPair.getPrivate());
        publicKeyStore.put(deviceUuid, keyPair.getPublic());

        return java.util.Base64.getEncoder()
                .encodeToString(keyPair.getPublic().getEncoded());
        
        Device device = new Device();
        deviceRepo.save(device);
    }

    public String sign(String deviceUuid,
                       String challenge,
                       String payload) throws Exception {

        PrivateKey privateKey = privateKeyStore.get(deviceUuid);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        signature.update(challenge.getBytes("UTF-8"));
        signature.update(payload.getBytes("UTF-8"));

        return java.util.Base64.getEncoder()
                .encodeToString(signature.sign());
    }
    
}
