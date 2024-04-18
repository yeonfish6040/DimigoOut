package com.yeonfish.multitool.services;

import com.yeonfish.multitool.beans.dao.StatusDAO;
import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.beans.vo.StatusVO;
import com.yeonfish.multitool.devController.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusManageService implements StatusManageServiceInter {
    private final logger log = new logger();

    @Autowired
    private StatusDAO statusDAO;

    @Override
    public boolean updateStatus(StatusVO statusVO) {
        StatusVO[] existsCheck = statusDAO.getStatus(statusVO);
        boolean is_exists = existsCheck.length != 0;

        boolean is_success = false;
        if (is_exists) {
            if (statusVO.getStatus() == existsCheck[0].getStatus())
                return statusDAO.delStatus(statusVO);
            boolean is_del_success = statusDAO.delStatus(statusVO);
            boolean is_set_success = statusDAO.setStatus(statusVO);
            if (is_del_success && is_set_success) is_success = true;
        }else {
            boolean is_set_success = statusDAO.setStatus(statusVO);
            if (is_set_success) is_success = true;
        }

        return is_success;
    }

    @Override
    public StatusVO[] getStatus(StatusVO statusVO) {
        return statusDAO.getStatus(statusVO);
    }

    @Override
    public StatusVO[] getClassStatusList(StatusVO statusVO) {
        return statusDAO.getClassStatusList(statusVO);
    }

    @Override
    public boolean delStatus(StatusVO statusVO) {
        return statusDAO.delStatus(statusVO);
    }
}
