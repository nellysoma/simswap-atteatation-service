/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.simswap.attestation.service;

import com.simswap.attestation.model.Decision;

/**
 *
 * @author Harmony
 */
public class FraudDecisionResult {
    
    private Decision decision;
    private String reason;
    private int riskScore;
    
    public FraudDecisionResult() {
    }
    
    public FraudDecisionResult(
            Decision decision,
            String reason,
            int riskScore
    ) {
        this.decision = decision;
        this.reason = reason;
        this.riskScore = riskScore;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getRiskScore() {
        return riskScore;
    }
}
