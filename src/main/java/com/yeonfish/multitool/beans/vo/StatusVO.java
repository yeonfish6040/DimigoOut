package com.yeonfish.multitool.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StatusVO {
    String id;
    int status;
    String reason;
}
