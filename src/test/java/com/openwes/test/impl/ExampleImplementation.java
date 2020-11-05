package com.openwes.test.impl;

import com.openwes.core.annotation.AutoInject;
import com.openwes.core.annotation.Implementation;
import com.openwes.test.ExampleInterface;
import com.openwes.test.TestInjectInterface;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation(source = ExampleInterface.class)
public class ExampleImplementation implements ExampleInterface {

    @AutoInject
    private TestInjectInterface testInjectInterface;

    @Override
    public String helloWorld() {
        testInjectInterface.print();
        return "Say hello world!!!";
    }

}
