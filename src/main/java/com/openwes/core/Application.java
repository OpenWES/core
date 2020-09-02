/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openwes.core;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Application {

    private final static Application APP = new Application();

    private Application() {

    }

    public final static Application newApp() {
        return APP;
    }

    public synchronized Application readConfig() {
        return this;
    }

    public synchronized Application readArg(String[] args) {
        return this;
    }

    public synchronized Application startIoc() {
        return this;
    }

    public synchronized Application bootstrap() {
        return this;
    }

    public synchronized void start() {

    }
}
