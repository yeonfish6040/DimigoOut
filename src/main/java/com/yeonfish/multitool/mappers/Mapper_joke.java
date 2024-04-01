package com.yeonfish.multitool.mappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mapper_joke {
    public String get(String id);
    public int set(String id);
}
