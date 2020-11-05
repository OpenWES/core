package com.openwes.test.impl;

import com.openwes.core.annotation.Implementation;
import com.openwes.test.TestInjectInterface;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation(of = TestInjectInterface.class)
public class TestInjectImplementation implements TestInjectInterface {

    @Override
    public void print() {
        System.out.println("Hello world. This is TestInjectImplementation");
    }

}
