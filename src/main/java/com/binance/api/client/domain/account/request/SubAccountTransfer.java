package com.binance.api.client.domain.account.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SubAccountTransfer {

    /**
     * Counter party name
     */
    private String counterParty;

    /**
     * Counter party email
     */
    private String email;

    /**
     * Transfer in or transfer out
     */
    private Integer type; // 1 for transfer in, 2 for transfer out

    /**
     * Transfer asset
     */
    private String asset;

    /**
     * Quantity of transfer asset
     */
    private String qty;

    /**
     * Transfer status
     */
    private String status;

    /**
     * Transfer ID
     */
    private Long tranId;

    /**
     * Transfer time
     */
    private Long time;

}