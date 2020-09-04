package com.openwes.test.impl;

import com.openwes.core.annotation.Implementation;
import com.openwes.test.ExampleInterface;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@Implementation(source = ExampleInterface.class)
public class ExampleImplementation implements ExampleInterface {

    @Override
    public String helloWorld() {
        return "Say hello world!!!";
    }

}
