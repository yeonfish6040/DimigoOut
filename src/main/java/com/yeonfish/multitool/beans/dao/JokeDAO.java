package com.yeonfish.multitool.beans.dao;

import com.yeonfish.multitool.mappers.Mapper_admin;
import com.yeonfish.multitool.mappers.Mapper_joke;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JokeDAO {

    @Autowired
    Mapper_joke jokeMapper;

    public String getJoke(String id) {
        return jokeMapper.get(id);
    }
    public boolean setJoke(String id) {
        return jokeMapper.set(id) == 1;
    }
}
