package com.yeonfish.multitool.services;

import com.yeonfish.multitool.beans.dao.AlimiDAO;
import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.devController.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlimManageService implements AlimManageServiceInter {
    private final logger log = new logger();

    @Autowired
    private AlimiDAO alimiDAO;

    @Override
    public boolean uploadAlim(AlimiVO alimiVO) {
        return alimiDAO.uploadAlim(alimiVO);
    }

    @Override
    public AlimiVO getAlim() {
        return alimiDAO.getAlim();
    }
}
