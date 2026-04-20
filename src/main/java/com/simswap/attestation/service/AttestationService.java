/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */

package com.simswap.attestation.service;

import com.simswap.attestation.model.Challenge;
import com.simswap.attestation.model.Device;
import com.simswap.attestation.repository.ChallengeRepository;
import com.simswap.attestation.repository.DeviceRepository;
import com.simswap.attestation.util.CryptoUtil;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Harmony
 */
@Service
public class AttestationService {

    private final DeviceRepository deviceRepo;
    private final ChallengeRepository challengeRepo;

    public AttestationService(DeviceRepository deviceRepo,ChallengeRepository challengeRepo) {
        this.deviceRepo = deviceRepo;
        this.challengeRepo = challengeRepo;
    }

    /**public void enroll(String deviceUuid, String publicKey) {

        Device device = new Device();
        device.setDeviceUuid(deviceUuid);
        device.setPublicKey(publicKey);

        deviceRepo.save(device);
    }**/

    public String generateChallenge(String deviceUuid) {

        String challenge = UUID.randomUUID().toString();

        Challenge ch = new Challenge();
        ch.setDeviceUuid(deviceUuid);
        ch.setChallenge(challenge);
        ch.setExpiresAt(Instant.now().plusSeconds(60));

        challengeRepo.save(ch);

        return challenge;
    }

    public boolean verify(String deviceUuid,
                          String challenge,
                          String payload,
                          String signature) {
        
            System.out.println("Before if statements");
            //System.out.println("PublicKeyString is: " + PublicKeyString );
            System.out.println("deviceUuid is: " + deviceUuid );
            System.out.println("challenge is: " + challenge );
            System.out.println("signature is: " + signature );

        try {

            Device device = deviceRepo.findById(deviceUuid).orElse(null);
            if (device == null || device.isRevoked()) return false;

            Challenge ch = challengeRepo
                    .findTopByDeviceUuidAndChallengeAndUsedFalseOrderByIdDesc(
                            deviceUuid, challenge);

            if (ch == null || ch.isUsed()) return false;

            if (ch.getExpiresAt().isBefore(Instant.now())) return false;
            
            String PublicKeyString =  deviceRepo.getPrivateKeyByDeviceUUID(deviceUuid);
            
            System.out.println("PublicKeyString is: " + PublicKeyString );
            System.out.println("deviceUuid is: " + deviceUuid );
            System.out.println("challenge is: " + challenge );
            System.out.println("signature is: " + signature );
            

            boolean valid = CryptoUtil.verifySignature(
                    PublicKeyString,
                    //device.getPublicKey(),
                    challenge,
                    payload,
                    signature
            );

            if (!valid) return false;

            ch.setUsed(true);
            challengeRepo.save(ch);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
