package com.yeonfish.multitool.beans.dao;

import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.mappers.Mapper_insert;
import com.yeonfish.multitool.mappers.Mapper_select;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlimiDAO {

    @Autowired
    Mapper_select selectMapper;

    @Autowired
    Mapper_insert insertMapper;

    public boolean uploadAlim(AlimiVO alimiVO) {
        return insertMapper.putAlim(alimiVO)==1;
    }

    public AlimiVO getAlim() {
        return selectMapper.getAlimi()[0];
    }
}
