package com.openwes.test;

import com.openwes.core.Application;
import com.openwes.core.IOC;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author xuanloc0511@gmail.com
 */
@RunWith(JUnit4.class)
public class TestIOC {

    @Before
    public void before() {
        Application.run();
    }

    @Test
    public void test() {
        ExampleInterface exam = IOC.resolve(ExampleInterface.class);
        String s = exam.helloWorld();
        Assert.assertEquals("Check message of interface", "Say hello world!!!", s);
    }
}
