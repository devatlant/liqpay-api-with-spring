package com.devatlant.model;

/**
 * class encapsulating 2 fields required by LiqPay to use their API - signature and data
 * Those fields should be used while sending the post request for opening the payment widget.
 */
public class LiqPayContract {
    public final String data;
    public final String signature;

    public LiqPayContract(String data,String signature) {
        this.data = data;
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "LiqPayContract{" +
            "signature='" + signature + '\'' +
            ", data='" + data + '\'' +
            '}';
    }
}
