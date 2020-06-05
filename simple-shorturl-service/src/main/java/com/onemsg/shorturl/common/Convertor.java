package com.onemsg.shorturl.common;

public class Convertor {

    static final char[] digits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
        'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
        'o' , 'p' , 'q' , 'r' , 's' , 't' ,
        'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
        'A' , 'B' , 'C' , 'D' , 'E' , 'F' , 
        'G' , 'H' , 'I' , 'J' , 'K' , 'L' , 
        'M' , 'N' , 'O' , 'P' , 'Q' , 'R' , 
        'S' , 'T' , 'U' , 'V' , 'W' , 'X' , 
        'Y' , 'Z'
    };

    /**
     * 十进制数字 转 62 进制
     * @param i
     * @return
     */
    public static String to62String(int i) {

        int radix = 62;
        char[] buf = new char[12]; //Integer.MAX_VALUE 大约是 62 的 11 次幂
        boolean negative = (i < 0);
        int charPos = 11;

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = digits[-(i % radix)];
            i = i / radix;
        }
        buf[charPos] = digits[-i];

        if (negative) {
            buf[--charPos] = '-';
        }

        return String.valueOf(buf, charPos, 12 - charPos);
    }

    /**
     * 给定字符串的 hashCode 转 62 进制
     * @param s
     * @return
     */
    public static String to62String(String s){
        int hash = Math.abs(s.hashCode());
        return to62String(hash);
    }

}