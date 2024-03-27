package com.yeonfish.multitool.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AlimiVO {
    int id;
    String date;
    String text;

    @Override
    public String toString() {
        return "{\"id\": \""+id+"\", \"date\":\""+date+"\", \"text\":\""+text+"\"}";
    }
}