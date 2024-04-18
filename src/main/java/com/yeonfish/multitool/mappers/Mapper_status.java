package com.yeonfish.multitool.mappers;

import com.yeonfish.multitool.beans.vo.StatusVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mapper_status {
    public StatusVO[] get(StatusVO statusVO);
    public StatusVO[] getList(StatusVO statusVO);
    public int set(StatusVO statusVO);
    public int del(StatusVO statusVO);
    public int clear();
}
