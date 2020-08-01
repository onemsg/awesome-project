package com.onemsg.infosharing;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class CommonTest {
    
    @Test
    public void testMap(){
        ConcurrentHashMap<String,String> cache = new ConcurrentHashMap<>();
        cache.put("python", "python spark sql.pdf");
        cache.forEach( (k,v) ->{
            System.out.println(k + ": " + v);
        });
    }
}