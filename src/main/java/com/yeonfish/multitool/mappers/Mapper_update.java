package com.yeonfish.multitool.mappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mapper_update {
    //@Select("select sysdate from dual")
    public String getTime();
}
