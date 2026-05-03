/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.simswap.attestation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Harmony
 */

@Entity
@Getter @Setter
public class DeviceHistory {
    
    @Id
    private String deviceUuid;

    private String lastMcc;
    private String lastMnc;
    private String lastCity;
    private String lastIp;

    private Instant lastSeen;
}
