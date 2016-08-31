package com.ougnt.period_manager.data;

import java.math.BigInteger;

/**
 * Created by wacharint on 8/29/2016 AD.
 */
public class RsaEncoder {

    private BigInteger e;
    private BigInteger n;

    public RsaEncoder(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    public String encrypt(String message) {

        String results = "";

        for(int i = 0; i < message.length(); i++){
            char c = message.toCharArray()[i];
            results += encrypt(c).toString(36) + "|";
        }

        if(results.length() > 0) {
            results = results.substring(0, results.length() - 1);
        }

        return results;
    }

    private BigInteger encrypt(int message) {

        return new BigInteger(message + "").modPow(e, n);
    }

    private BigInteger encrypt(char c) {

        return encrypt((int)c);
    }
}
