package com.yeonfish.multitool.services;

import com.yeonfish.multitool.beans.vo.AlimiVO;
import org.springframework.stereotype.Service;

@Service
public interface AlimManageServiceInter {
    public boolean uploadAlim(AlimiVO alimiVO);
    public AlimiVO getAlim();
}
