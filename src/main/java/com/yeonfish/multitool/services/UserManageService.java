package com.yeonfish.multitool.services;

import com.yeonfish.multitool.beans.dao.UserDAO;
import com.yeonfish.multitool.beans.vo.UserVO;
import com.yeonfish.multitool.devController.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManageService implements UserManageServiceInter {
    private final logger log = new logger();

    @Autowired
    UserDAO userDAO;

    @Override
    public UserVO getLoggedInUser(UserVO user) {
        UserVO tmp = new UserVO();
        tmp.setSession(user.getSession());
        return userDAO.get(tmp);
    }

    @Override
    public UserVO getUser(UserVO user) {
        UserVO tmp = new UserVO();
        tmp.setId(user.getId());
        return userDAO.get(tmp);
    }

    @Override
    public boolean setUser(UserVO user) {
        UserVO tmp = new UserVO();
        tmp.setId(user.getId());
        UserVO result = this.getUser(tmp);
        if (result == null)
            return userDAO.set(user);
        else
            return userDAO.update(user);

    }

    @Override
    public boolean updateUserLoginStatus(UserVO user) {
        return userDAO.update(user);
    }

    @Override
    public boolean adminUser(UserVO user) {
        return userDAO.setAdmin(user);
    }

    @Override
    public boolean disAdminUser(UserVO user) {
        return userDAO.deleteAdmin(user);
    }

    @Override
    public UserVO[] getAdminUsers() {
        return userDAO.getAdmins();
    }

    @Override
    public boolean isAdmin(UserVO user) {
        UserVO[] admins = this.getAdminUsers();
        for (UserVO admin : admins) {
            if (admin.getId().equals(user.getId())) return true;
        }

        return false;
    }
}
