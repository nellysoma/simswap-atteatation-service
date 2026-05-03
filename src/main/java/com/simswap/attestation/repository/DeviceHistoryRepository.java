/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.simswap.attestation.repository;

import com.simswap.attestation.model.DeviceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Harmony
 */
public interface DeviceHistoryRepository extends JpaRepository<DeviceHistory, String> {
    
}
