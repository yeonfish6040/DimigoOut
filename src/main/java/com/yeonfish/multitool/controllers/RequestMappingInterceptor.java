package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.Constant;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.util.UUID;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestMappingInterceptor implements HandlerInterceptor {

    private logger log = new logger();

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
        HttpSession session = request.getSession();

        if (request.getRequestURI().endsWith("/auth"))
            return true;

        if (session.getAttribute(sessionId) == null) {
            String state = UUID.generate();
//            session.setAttribute("state", ((List<String>)(session.getAttribute("state"))).add(state));
//            response.sendRedirect("https://accounts.google.com/o/oauth2/v2/auth?state="+state+"&client_id="+Constant.GoogleOauthClientId+"&response_type=code&hd=dimigo.hs.kr&redirect_uri=https://dimigo.site/auth&scope=email profile openid");
//            response.sendRedirect("https://accounts.google.com/o/oauth2/v2/auth?state="+state+"&client_id="+Constant.GoogleOauthClientId+"&response_type=code&hd=dimigo.hs.kr&redirect_uri=https://localhost/auth&scope=email profile openid");
            response.sendRedirect("https://auth.dimigo.net/oauth?client="+Constant.DimigoInClientId+"&redirect=https://localhost/auth");
//            response.sendRedirect("https://auth.dimigo.net/oauth?client="+Constant.DimigoInClientId+"&redirect=https://dimigo.site/auth");

            return false;
        }

        return true;
    }
}
