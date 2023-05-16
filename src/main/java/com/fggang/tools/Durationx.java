package com.fggang.tools;

import java.time.Duration;

public class Durationx {


    public String toString(Duration duration) {
        long s = duration.getSeconds();
        long h = s / 3600;
        long m = (s - h*3600) / 60;
        s = s - (h*3600 + m*60);
        String result = "";
        if(h != 0) {
            result +=Math.abs(h) + "时";
        }
        if(m != 0) {
            result +=Math.abs(m) + "分";
        }
        if(s != 0) {
            result +=Math.abs(s) + "秒";
        }

        return result;
    }
}
