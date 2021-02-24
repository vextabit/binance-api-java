package com.binance.api.client.domain.account;

import java.util.List;

import lombok.Data;

/**
 * Represents a response from the call to dust transfer (swapping tiny untradeable amounts of coins for BNB).
 *
 * @author sergey
 *
 */
@Data
public class DustTransferResponse {

  double totalServiceCharge;

  double totalTransfered;

  List<DustTransferResult> transferResult;

}
