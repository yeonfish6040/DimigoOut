package com.yeonfish.multitool.devController;

import com.yeonfish.multitool.util.Colors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class logger {
    private boolean logging = true;

    private boolean withClass = true;
    private boolean isTest = false;

    public logger() {

    }

    public logger(boolean withClass) {
        this.withClass = withClass;
    }
    public logger(boolean withClass, boolean isTest) {
        this.withClass = withClass;
        this.isTest = isTest;
    }

    public boolean doLog() {
        return logging;
    }

    public void info(Object e) {
        if (doLog() && e != null)
            log.info(Colors.GREEN+(withClass?getCallerClassName():"")+Colors.END+": "+e.toString());

    }

    public void error(Object e) {
        if (doLog() && e != null)
            log.error(Colors.RED+(withClass?getCallerClassName():"")+": "+e.toString()+Colors.END);
    }

    public void title(Object e) {
        if (doLog() && e != null)
            log.info(Colors.GREEN+(withClass?getCallerClassName():"")+Colors.END+": "+"\n===============================================\n"+e.toString()+"\n===============================================\n");
    }

    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(logger.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
    }

}
