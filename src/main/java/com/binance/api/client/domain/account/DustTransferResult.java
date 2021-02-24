package com.binance.api.client.domain.account;

import lombok.Data;

/**
 * Individual small amount exchange for BNB.
 *
 * @author sergey
 *
 * @see DustTransferResponse
 */
@Data
public class DustTransferResult {

  double amount;
  String fromAsset;
  long operateTime;
  double serviceChargeAmount;
  long tranId;
  double transferedAmount;

}
