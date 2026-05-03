/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.simswap.attestation.service;

import com.simswap.attestation.model.Decision;
import com.simswap.attestation.model.DeviceHistory;
import com.simswap.attestation.repository.DeviceHistoryRepository;
import java.time.Instant;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Harmony
 */

@Component
public class FraudDecisionEngine {
    
    private final DeviceHistoryRepository historyRepo;
    
    public FraudDecisionEngine(DeviceHistoryRepository historyRepo) {
        this.historyRepo = historyRepo;
    }
    
    public Decision evaluate(String deviceUuid, String payloadJson) {
        
        JSONObject payload = new JSONObject(payloadJson);
        
        int riskScore = 0;

        String mcc = payload.optString("mcc", null);
        String mnc = payload.optString("mnc", null);
        String city = payload.optString("city", null);
        String ip = payload.optString("ipAddress", null);

        boolean isRooted = payload.optBoolean("isRooted", false);
        boolean isEmulator = payload.optBoolean("isEmulator", false);
        boolean usbDebug = payload.optBoolean("usbDebug", false);

        DeviceHistory history = historyRepo.findById(deviceUuid).orElse(null);

        // 🚨 1. First time device
        if (history == null) {

            history = new DeviceHistory();
            history.setDeviceUuid(deviceUuid);

            updateHistory(history, mcc, mnc, city, ip);

            historyRepo.save(history);

            return Decision.ALLOW; // first-time trusted enrollment
        }

        // 🚨 2. SIM swap detection
        if (history.getLastMcc() != null && history.getLastMnc() != null) {

            boolean simChanged =
                    !history.getLastMcc().equals(mcc) ||
                    !history.getLastMnc().equals(mnc);

            if (simChanged) {
                riskScore += 50;
            }
        }

        // 🚨 3. Location anomaly
        if (history.getLastCity() != null && city != null &&
                !history.getLastCity().equalsIgnoreCase(city)) {

            riskScore += 20;
        }

        // 🚨 4. IP change
        if (history.getLastIp() != null && ip != null &&
                !history.getLastIp().equals(ip)) {

            riskScore += 10;
        }

        // 🚨 5. Device risk signals
        if (isRooted) riskScore += 40;
        if (isEmulator) riskScore += 60;
        if (usbDebug) riskScore += 10;

        // 🧠 Decision thresholds
        Decision decision;

        if (riskScore >= 80) {
            decision = Decision.HIGH_RISK;
        } else if (riskScore >= 40) {
            decision = Decision.CHALLENGE;
        } else {
            decision = Decision.ALLOW;
        }

        // 🔄 Update history after evaluation
        //updateHistory(history, mcc, mnc, city, ip);
        //historyRepo.save(history);
        if (decision == Decision.ALLOW) {
            updateHistory(history, mcc, mnc, city, ip);
            historyRepo.save(history);
        }

        return decision;
    }
    
    private void updateHistory(DeviceHistory history,
                               String mcc,
                               String mnc,
                               String city,
                               String ip) {

        history.setLastMcc(mcc);
        history.setLastMnc(mnc);
        history.setLastCity(city);
        history.setLastIp(ip);
        history.setLastSeen(Instant.now());
    }

}
