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
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

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
        
        Device device = new Device();
        
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        privateKeyStore.put(deviceUuid, keyPair.getPrivate());
        publicKeyStore.put(deviceUuid, keyPair.getPublic());
        
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        
        device.setDeviceUuid(deviceUuid);
        device.setPublicKey(publicKeyString);
        device.setPrivateKey(privateKeyString);
        
        deviceRepo.save(device);

        return java.util.Base64.getEncoder()
                .encodeToString(keyPair.getPublic().getEncoded());
        
    }

    public String sign(String deviceUuid,
                       String challenge,
                       String payload) throws Exception {
        
        
        String PrivateKeyString =  deviceRepo.getPrivateKeyByDeviceUUID(deviceUuid);
        byte[] bytes = Base64.getDecoder().decode(PrivateKeyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);
        
        //PrivateKey privateKey = privateKeyStore.get(deviceUuid);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        signature.update(challenge.getBytes("UTF-8"));
        signature.update(payload.getBytes("UTF-8"));

        return java.util.Base64.getEncoder()
                .encodeToString(signature.sign());
    }
    
}
