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

import java.util.ArrayList;
import java.util.Arrays;

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

        if (
                (request.getRequestURI().startsWith("/public") && !request.getRequestURI().contains(".."))
                ||
                (request.getRequestURI().startsWith("/sounds") && !request.getRequestURI().contains(".."))
            ) return true;


//        String otp = "";
//        if (request.getQueryString() != null) {
//            String[] queries = request.getQueryString().split("&");
//            for (String query : queries) {
//                if (query.split("=").length == 2 && query.split("=")[0].equals("otp")) {
//                    otp = query.split("=")[1];
//                }
//            }
//        }
//
//        boolean otpSession = false;
//        HttpSession session = request.getSession();
//        if (!otp.isEmpty() && session.getAttribute("otp") != null) {
//            ArrayList<Integer> otps = (ArrayList<Integer>) session.getAttribute("otp");
//            if (otps.contains(otp)) {
//                otps.remove(otp);
//                otpSession = true;
//            }
//        }
//
//        if (otpSession || (session.getAttribute("sessions") != null && ((ArrayList<String>)session.getAttribute("sessions")).contains(sessionId))) {
//            if (session.getAttribute("sessions") == null) {
//                ArrayList<String> sessions = new ArrayList<>();
//                if (otpSession) sessions.add(sessionId);
//                session.setAttribute("sessions", sessions);
//            }else if (otpSession) {
//                ArrayList<String> sessions = (ArrayList<String>)session.getAttribute("sessions");
//                sessions.add(sessionId);
//                session.setAttribute("sessions", sessions);
//            }
//            if (session.getAttribute("sessions") != null && ((ArrayList<String>)session.getAttribute("sessions")).contains(sessionId))
//                return true;
//        }

        UserVO tmp = new UserVO();
        tmp.setSession(sessionId);
        if (userManageService.getLoggedInUser(tmp) == null) {
            if (request.getMethod().equals(RequestMethod.POST.name())) {
                response.sendError(401, "Login required");
                return false;
            }

//            response.sendRedirect("https://auth.dimigo.net/oauth?client="+Constant.DimigoInClientId+"&redirect=https://localhost/public/auth");
            response.sendRedirect("https://auth.dimigo.net/oauth?client="+Constant.DimigoInClientId+"&redirect=https://dimigo.site/public/auth");

            return false;
        }

        return true;
    }
}
