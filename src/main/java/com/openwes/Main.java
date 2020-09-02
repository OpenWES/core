/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openwes;

import com.openwes.core.Application;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Main {

    public static void main(String[] args) {
        Application.newApp()
                .readConfig()
                .readArg(args)
                .startIoc()
                .bootstrap()
                .start();
    }

}
