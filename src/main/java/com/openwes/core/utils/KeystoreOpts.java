/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openwes.core.utils;

/**
 * 
 * @author xuanloc0511@gmail.com
 */
public final class KeystoreOpts {

    private final String keystorePath;
    private final String keystorePass;
    private final String keystoreType;
    private final String keystoreAlias;

    public KeystoreOpts(String keystorePath, String keystorePass, String keystoreType, String keystoreAlias) {
        this.keystorePath = keystorePath;
        this.keystorePass = keystorePass;
        this.keystoreType = keystoreType;
        this.keystoreAlias = keystoreAlias;
    }

    public String getKeystorePass() {
        return keystorePass;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getKeystoreType() {
        return keystoreType;
    }

    public String getKeystoreAlias() {
        return keystoreAlias;
    }

}
