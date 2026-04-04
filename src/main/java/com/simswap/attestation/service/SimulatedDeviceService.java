/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */

package com.simswap.attestation.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author Harmony
 */
@Service
public class SimulatedDeviceService {

    private final Map<String, PrivateKey> privateKeyStore = new HashMap<>();
    private final Map<String, PublicKey> publicKeyStore = new HashMap<>();
    
    
}
