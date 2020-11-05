package com.openwes.test.impl;

import com.openwes.core.annotation.Implementation;
import com.openwes.test.ExampleInterface;
import com.openwes.test.TestInjectInterface;
import com.openwes.core.annotation.Inject;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation(of = ExampleInterface.class)
public class ExampleImplementation implements ExampleInterface {

    @Inject
    private TestInjectInterface testInjectInterface;

    @Override
    public String helloWorld() {
        testInjectInterface.print();
        return "Say hello world!!!";
    }

}
