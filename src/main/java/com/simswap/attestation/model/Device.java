/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.simswap.attestation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Harmony
 */

@Entity
@Getter @Setter
public class Device {

    @Id
    private String deviceUuid;

    //private String publicKey;

    private PublicKey publicKey;
    
    private PrivateKey privateKey;
    
    private Instant createdAt = Instant.now();

    private boolean revoked = false;
}
