package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.beans.dao.StatusDAO;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.services.AlimManageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

    private final logger log = new logger();

    @Autowired
    AlimManageService alimManageService;

    @Autowired
    StatusDAO statusDAO;

    @RequestMapping("/check_log")
    public String logCheck() {
        return "logCheck";
    }

    @RequestMapping("/seat_change")
    public String seatChange() {
        return "seat_change";
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) throws JSONException {
//        log.info(alimManageService.getAlim());
        model.addAttribute("alim", alimManageService.getAlim());
        return "new";
    }

    @RequestMapping("/public/timer_core")
    public String timer() { return "timer"; }

    @RequestMapping("/public/timer")
    public String timerPublic() { return "timer_public"; }

    private String getSessionId(Cookie[] cookies) {
        String sessionId = "";
        if (cookies != null)
            for (Cookie c : cookies)
                if (c.getName().equals("sessionId")) {
                    sessionId = c.getValue();
                }

        return sessionId;
    }
}
