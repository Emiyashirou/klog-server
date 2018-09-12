package com.klog.basic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test1 {

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    @Test
    public void sampleTest() {
        assertEquals("a", "a");
        System.out.println("sample test");
    }
}
