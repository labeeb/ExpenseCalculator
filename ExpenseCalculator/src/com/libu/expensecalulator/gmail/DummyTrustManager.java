package com.libu.expensecalulator.gmail;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class DummyTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] cert, String authType) {
    // everything is trusted
    }

    public void checkServerTrusted(X509Certificate[] cert, String authType) {
    // everything is trusted
    }

    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    	return new java.security.cert.X509Certificate[0];
    }
}
