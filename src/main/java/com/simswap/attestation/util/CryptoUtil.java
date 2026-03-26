/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.simswap.attestation.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author Harmony
 */
public class CryptoUtil {
    
    public static boolean verifySignature(
            String publicKeyBase64,
            String challenge,
            String payload,
            String signatureBase64
    ) throws Exception {
        
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(spec);

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);

        sig.update(challenge.getBytes("UTF-8"));
        sig.update(payload.getBytes("UTF-8"));

        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

        return sig.verify(signatureBytes);
    }

}
