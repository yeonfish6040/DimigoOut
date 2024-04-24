package com.yeonfish.multitool.beans.dao;

import com.yeonfish.multitool.beans.vo.UserVO;
import com.yeonfish.multitool.mappers.Mapper_user;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    @Autowired
    private Mapper_user userMapper;

    public UserVO get(UserVO userVO) { UserVO[] tmp = userMapper.get(userVO); return tmp.length == 0 ? null : tmp[0]; }
    public boolean set(UserVO userVO) { return userMapper.set(userVO) == 1; }
    public boolean update(UserVO userVO) { return userMapper.update(userVO) == 1; }
    public UserVO[] getAdmins() { return userMapper.getAdmins(); }
    public boolean setAdmin(UserVO userVO) { return userMapper.setAdmin(userVO) == 1; }
    public boolean deleteAdmin(UserVO userVO) { return userMapper.deleteAdmin(userVO) == 1; }
}
