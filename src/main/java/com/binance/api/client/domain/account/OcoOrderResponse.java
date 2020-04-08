package com.binance.api.client.domain.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned when a new OCO order is placed.
 * 
 * @author sergey
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcoOrderResponse {

    long orderListId;
    String contingencyType;
    String listStatusType;
    String listOrderStatus;
    String listClientOrderId;
    long transactionTime;
    String symbol;
    List<OcoLeg> orders;
    List<Order> orderReports;

    public static class OcoLeg {

        String symbol;
        long orderId;
        String clientOrderId;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getClientOrderId() {
            return clientOrderId;
        }

        public void setClientOrderId(String clientOrderId) {
            this.clientOrderId = clientOrderId;
        }

    }

    public long getOrderListId() {
        return orderListId;
    }

    public void setOrderListId(long orderListId) {
        this.orderListId = orderListId;
    }

    public String getContingencyType() {
        return contingencyType;
    }

    public void setContingencyType(String contingencyType) {
        this.contingencyType = contingencyType;
    }

    public String getListStatusType() {
        return listStatusType;
    }

    public void setListStatusType(String listStatusType) {
        this.listStatusType = listStatusType;
    }

    public String getListOrderStatus() {
        return listOrderStatus;
    }

    public void setListOrderStatus(String listOrderStatus) {
        this.listOrderStatus = listOrderStatus;
    }

    public String getListClientOrderId() {
        return listClientOrderId;
    }

    public void setListClientOrderId(String listClientOrderId) {
        this.listClientOrderId = listClientOrderId;
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<OcoLeg> getOrders() {
        return orders;
    }

    public void setOrders(List<OcoLeg> orders) {
        this.orders = orders;
    }

    public List<Order> getOrderReports() {
        return orderReports;
    }

    public void setOrderReports(List<Order> orderReports) {
        this.orderReports = orderReports;
    }
    
}
