package com.github.tcking.giraffe.helper;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by tc(mytcking@gmail.com) on 15/6/20.
 */
public class PlaceHolderPropertiesTest extends TestCase {
    private PlaceHolderProperties placeHolderProperties=new PlaceHolderProperties();


    @Before
    public void setUp() throws Exception {
        placeHolderProperties.load(new FileInputStream(new File("/Users/tc/work/tmp/test.properties")));
    }

    @Test
    public void testGetProperty() throws Exception {
        String test = placeHolderProperties.getProperty("image_cache");
        System.out.println(test);
        assertEquals("/giraffe/cache/imageCache",test);
    }
}