package com.vodafone.SpringBootDemo;

public class PageUtil {
    public static Integer validPage(Integer page){
        if (page==null || page<0){
            return 0;
        }
        return page;
    }
    public static Integer validSize(Integer size){
        if (size==null || size<1){
            return 10;
        }
        return size;
    }
}
