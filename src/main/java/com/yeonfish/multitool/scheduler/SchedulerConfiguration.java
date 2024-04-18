package com.yeonfish.multitool.scheduler;

import com.yeonfish.multitool.beans.dao.StatusDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerConfiguration {
    @Autowired
    StatusDAO statusDAO;

    @Scheduled(cron="0 0 2 * * ?" )
    public void resetLog() {
        statusDAO.clearStatus();
    }
}
