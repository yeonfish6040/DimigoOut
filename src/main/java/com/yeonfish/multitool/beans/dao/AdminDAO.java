package com.yeonfish.multitool.beans.dao;

import com.yeonfish.multitool.mappers.Mapper_admin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminDAO {

    @Autowired
    Mapper_admin adminMapper;

    public String getAdmin(String id) {
        return adminMapper.get(id);
    }
}
