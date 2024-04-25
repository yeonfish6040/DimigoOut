package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.Constant;
import com.yeonfish.multitool.beans.vo.UserVO;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.services.UserManageService;
import com.yeonfish.multitool.util.UUID;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestMappingInterceptor implements HandlerInterceptor {

    private logger log = new logger();

    @Autowired
    private UserManageService userManageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = UUID.generate();
        Cookie[] cookies = request.getCookies();
        boolean hasCookie = false;
        if (cookies != null)
            for (Cookie c : cookies)
                if (c.getName().equals("sessionId")) {
                    hasCookie = true;
                    sessionId = c.getValue();
                }

        if (!hasCookie)
            response.addCookie(new Cookie("sessionId", sessionId));

        if (request.getRequestURI().endsWith("/auth"))
            return true;

        UserVO tmp = new UserVO();
        tmp.setSession(sessionId);
        if (userManageService.getLoggedInUser(tmp) == null) {
            if (request.getMethod().equals(RequestMethod.POST.name())) {
                response.sendError(401, "Login required");
                return false;
            }

//            response.sendRedirect("https://auth.dimigo.net/oauth?client="+Constant.DimigoInClientId+"&redirect=https://localhost/auth");
            response.sendRedirect("https://auth.dimigo.net/oauth?client="+Constant.DimigoInClientId+"&redirect=https://dimigo.site/auth");

            return false;
        }

        return true;
    }
}
