package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.devController.logger;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
public class CommonExceptionAdvice {
    private final logger log = new logger();

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String except(Exception e, Model model) {

        StringBuffer err = new StringBuffer();
        err.append("\n==================================ERROR OCCURRED==================================\n<br>");
        err.append(getCallerClassName()+" - "+e.getClass()+"\n");
        err.append("Reason: "+e.getMessage()+"\n");
        Arrays.stream(e.getStackTrace()).toList().forEach((er) -> {
            err.append(er+"\n");
        });
        err.append("==================================ERROR OCCURRED==================================");
        log.error(err.toString());

        model.addAttribute("Exception", e);
        model.addAttribute("errmsg", e.getLocalizedMessage());
        model.addAttribute("errs", err.toString());

        return null;
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
