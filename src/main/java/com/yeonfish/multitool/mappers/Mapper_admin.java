package com.yeonfish.multitool.mappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mapper_admin {
    public String get(String id);
}
