/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.simswap.attestation.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 *
 * @author Harmony
 */
public class CryptoUtil {
    
    public static boolean verifySignature(
            String publicKeyBase64,
            //PublicKey publicKeyBase64,
            String challenge,
            String payload,
            String signatureBase64
    ) throws Exception {
        
        System.out.println("Public Key inside CryptoUtil: " + publicKeyBase64);
        System.out.println("challenge inside CryptoUtil: " + challenge);
        System.out.println("payload inside CryptoUtil: " + payload);
        System.out.println("signatureBase64 inside CryptoUtil: " + signatureBase64);
       
        // convert string to bytes
        //byte[] keyBytes = publicKeyBase64.getEncoded();
        
        //convert string to PublicKey
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        System.out.println("Key bytes length: " + keyBytes.length);
        
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        System.out.println("KeySpec created successfully");
       
        KeyFactory kf = KeyFactory.getInstance("RSA");
        System.out.println("KeyFactory algorithm: " + kf.getAlgorithm());
        
        
        //PublicKey publicKey = kf.generatePublic(spec);
        
        PublicKey publicKey = null;
        
        try {
            publicKey = kf.generatePublic(spec);
            System.out.println("Public key generated successfully");
        } catch (Exception e) {
            System.out.println("Failed to generate public key");
            e.printStackTrace();
        }
        
        Signature sig = Signature.getInstance("SHA256withRSA");
        //sig.initVerify(publicKey);
        
        try {
            sig.initVerify(publicKey);
            System.out.println("initVerify successful");
        } catch (Exception e) {
            System.out.println("initVerify failed");
            e.printStackTrace();
        }
        
        System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
        System.out.println("Public Key Format: " + publicKey.getFormat());

        sig.update(challenge.getBytes("UTF-8"));
        sig.update(payload.getBytes("UTF-8"));
        
        System.out.println("Challenge bytes: " + Arrays.toString(challenge.getBytes("UTF-8")));
        System.out.println("Payload bytes: " + Arrays.toString(payload.getBytes("UTF-8")));

        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
        System.out.println("Signature length: " + signatureBytes.length);

        return sig.verify(signatureBytes);
    }

}
