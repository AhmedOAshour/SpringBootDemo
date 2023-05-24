package com.vodafone.SpringBootDemo;

public class PageUtil {
    public static void defaultPageSize(Integer page, Integer size){
        if (page==null || page<0){
            page = Integer.valueOf(0);
        }
        if (size==null || size<1){
            size = Integer.valueOf(10);
        }
    }
}
