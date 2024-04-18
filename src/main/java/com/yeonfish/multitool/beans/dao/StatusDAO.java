package com.yeonfish.multitool.beans.dao;

import com.yeonfish.multitool.beans.vo.StatusVO;
import com.yeonfish.multitool.mappers.Mapper_status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StatusDAO {

    @Autowired
    Mapper_status statusMapper;

    public StatusVO[] getStatus(StatusVO statusVO) {
        return statusMapper.get(statusVO);
    }
    public boolean setStatus(StatusVO statusVO) {
        return statusMapper.set(statusVO) == 1;
    }
    public boolean delStatus(StatusVO statusVO) {
        return statusMapper.del(statusVO) == 1;
    }
}
