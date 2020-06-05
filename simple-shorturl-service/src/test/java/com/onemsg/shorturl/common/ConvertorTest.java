package com.onemsg.shorturl.common;

import org.junit.Test;

public class ConvertorTest {

    @Test
    public void testToString62(){

        int[] ints = {16, 31, 61, 65};
        String url = "https://www.runoob.com/python3/python4-ascii-character.html";

        int urlHashInt = Math.abs(url.hashCode());
        System.out.println(urlHashInt + " -> " + Convertor.to62String(urlHashInt));


        for(int i : ints){
            String s = Convertor.to62String(i);
            System.out.println(i + " -> " + s);
        }
    }
}