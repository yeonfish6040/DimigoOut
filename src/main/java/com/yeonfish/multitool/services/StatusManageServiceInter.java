package com.yeonfish.multitool.services;

import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.beans.vo.StatusVO;
import org.springframework.stereotype.Service;

@Service
public interface StatusManageServiceInter {
    public boolean updateStatus(StatusVO statusVO);
    public StatusVO[] getStatus(StatusVO statusVO);
    public StatusVO[] getClassStatusList(StatusVO statusVO);
    public boolean delStatus(StatusVO statusVO);
}
