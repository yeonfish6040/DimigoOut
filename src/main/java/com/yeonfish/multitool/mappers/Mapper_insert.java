package com.yeonfish.multitool.mappers;

import com.yeonfish.multitool.beans.vo.AlimiVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mapper_insert {
    public int putAlim(AlimiVO locationVO);
}
