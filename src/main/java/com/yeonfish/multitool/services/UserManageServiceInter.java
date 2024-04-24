package com.yeonfish.multitool.services;

import com.yeonfish.multitool.beans.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public interface UserManageServiceInter {

    public UserVO getLoggedInUser(UserVO user);
    public UserVO getUser(UserVO user);
    public boolean setUser(UserVO user);
    public boolean updateUserLoginStatus(UserVO user);

    public boolean adminUser(UserVO user);
    public boolean disAdminUser(UserVO user);
    public UserVO[] getAdminUsers();
    public boolean isAdmin(UserVO user);
}
