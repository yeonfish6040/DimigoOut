package com.yeonfish.multitool.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserVO {
    String id;
    String number;
    String name;
    String gender;
    String type;
    String profile_image;
    String session;
}
