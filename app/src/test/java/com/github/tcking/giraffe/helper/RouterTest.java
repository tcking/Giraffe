package com.github.tcking.giraffe.helper;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/13.
 */
public class RouterTest extends TestCase{

    @Test
    public void testParseComponentName() {
        assertEquals("main",Router.parseComponentName(Router.APP_SCHEMA + "main"));
        assertEquals("main",Router.parseComponentName(Router.APP_SCHEMA + "main?a=b&c=d?"));
    }

}