/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.simswap.attestation.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;

import com.simswap.attestation.service.SimulatedDeviceService;
import java.util.Map;

/**
 *
 * @author Harmony
 */
@RestController
@RequestMapping("/api/v1/test")
public class SimulatedDeviceController {

    private final SimulatedDeviceService simulatedDeviceService;

    public SimulatedDeviceController(SimulatedDeviceService simulatedDeviceService) {
        this.simulatedDeviceService = simulatedDeviceService;
    }

    @PostMapping("/simulate-enroll")
    public Map<String, String> enroll(@RequestBody Map<String, String> body) throws Exception {

        String deviceUuid = body.get("deviceUuid");

        String publicKey = simulatedDeviceService.enrollDevice(deviceUuid);

        return Map.of(
                "deviceUuid", deviceUuid,
                "publicKeyBase64", publicKey
                
        );
    }

    @PostMapping("/simulate-sign")
    public Map<String, String> sign(@RequestBody Map<String, String> body) throws Exception {

        String signature = simulatedDeviceService.sign(
                body.get("deviceUuid"),
                body.get("challenge"),
                body.get("payload")
        );

        return Map.of("signatureBase64", signature);
    }
}
