/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.simswap.attestation.repository;

import com.simswap.attestation.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Harmony
 */
public interface DeviceRepository extends JpaRepository<Device, String> {

    @Query("SELECT d.privateKey FROM Device d WHERE d.deviceUuid = :deviceUuid")
    String getPrivateKeyByDeviceUUID(@Param("deviceUuid") String deviceUuid);
    
    @Query("SELECT d.publicKey FROM Device d WHERE d.deviceUuid = :deviceUuid")
    String getPublicKeyByDeviceUUID(@Param("deviceUuid") String deviceUuid);
} 
