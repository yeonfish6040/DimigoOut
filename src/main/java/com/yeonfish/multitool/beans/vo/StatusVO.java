package com.yeonfish.multitool.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Calendars;

import java.util.Locale;

@Data
@Component
public class StatusVO {
    String time;
    String id;
    String name;
    String number;
    int status;
    String reason;

    public void setTime() {
        Calendars calendars = new Calendars(Locale.KOREAN);
        this.time = calendars.format(calendars.createNow(), "hh시 mm분 ss초");
    }

    @Override
    public String toString() {
        return String.format("{\"time\": \"%s\", \"id\": \"%s\", \"name\": \"%s\", \"number\": \"%s\", \"status\": \"%d\", \"reason\": \"%s\"}", time, id, name, number, status, reason);
    }
}
