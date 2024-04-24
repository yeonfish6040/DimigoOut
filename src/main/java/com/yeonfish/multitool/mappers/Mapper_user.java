package com.yeonfish.multitool.mappers;

import com.yeonfish.multitool.beans.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mapper_user {
    public UserVO[] get(UserVO userVO);
    public int set(UserVO userVO);
    public int update(UserVO userVO);
    public UserVO[] getAdmins();
    public int setAdmin(UserVO userVO);
    public int deleteAdmin(UserVO userVO);
}
