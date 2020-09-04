package com.openwes.test;

import com.openwes.core.annotation.Implementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
