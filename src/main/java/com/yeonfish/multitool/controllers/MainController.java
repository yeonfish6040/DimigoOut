package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.Constant;
import com.yeonfish.multitool.beans.dao.AdminDAO;
import com.yeonfish.multitool.beans.dao.AlimiDAO;
import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.services.AlimManageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.expression.Calendars;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;


@RestController
@RequestMapping("/")
public class MainController {
    private final logger log = new logger();

    @Autowired
    private AlimManageService alimManageService;

    @Autowired
    private AdminDAO adminDAO;

    @RequestMapping("/auth")
    public String auth(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
        JSONObject params = new JSONObject();
        params.put("code", code);
        params.put("client_id", Constant.GoogleOauthClientId);
        params.put("client_secret", Constant.GoogleOauthClientPw);
        params.put("grant_type", "authorization_code");
//        params.put("redirect_uri", "https://localhost/auth");
        params.put("redirect_uri", "https://dimigo.site/auth");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(params.toString(), headers);

        RestTemplate rt = new RestTemplate();

//        log.info(params.toString());

        ResponseEntity<String> exchange = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                entity,
                String.class
        );

        String access_token = (new JSONObject(exchange.getBody())).getString("access_token");

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer"+access_token);
        entity = new HttpEntity<>("", headers);

        rt = new RestTemplate();
        exchange = rt.exchange(
                "https://www.googleapis.com/oauth2/v1/userinfo?alt=json",
                HttpMethod.GET,
                entity,
                String.class
        );

        log.info("Login "+exchange.getBody());

        JSONObject result = new JSONObject(exchange.getBody());

        String sessionId = getSessionId(request.getCookies());
        HttpSession session = request.getSession();
        session.setAttribute(sessionId, result.toString());

        response.sendRedirect("/");
        return "success";
    }

    @RequestMapping(value = "alim/save", method = RequestMethod.POST)
    public String saveAlim(@RequestParam("text") String text, HttpServletRequest request, HttpServletResponse response) throws JSONException {
        HttpSession session = request.getSession();
        JSONObject user = new JSONObject((String) session.getAttribute(getSessionId(request.getCookies())));

        if (adminDAO.getAdmin(user.getString("id")) == null || adminDAO.getAdmin(user.getString("id")).equals("")){
            response.setStatus(403);
            return "Access Denied";
        }else {
            AlimiVO alim = new AlimiVO();
            Calendars calendars = new Calendars(Locale.KOREA);
            alim.setDate(calendars.format(calendars.createNow(), "MM월 dd일 hh시 mm분"));
            alim.setText(text);
            boolean isSuccess = alimManageService.uploadAlim(alim);
            if (isSuccess) {
                response.setStatus(200);
                return "Success";
            }else {
                response.setStatus(503);
                return "Server cannot process your request";
            }
        }
    }

//    @RequestMapping("get/dimiOutside")
//    public String getDimiOutSide() {
//
//    }

    @RequestMapping(value = "get/background", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage() throws IOException {
        InputStream in = null;
        if (LocalTime.now(ZoneId.of("Asia/Seoul")).isBefore(LocalTime.of(13, 0)) && LocalTime.now(ZoneId.of("Asia/Seoul")).isAfter(LocalTime.of(4, 0)))
            in = new ClassPathResource("static/image/background_morning.jpg").getInputStream();
        else if (LocalTime.now(ZoneId.of("Asia/Seoul")).isBefore(LocalTime.of(17, 0)) && LocalTime.now(ZoneId.of("Asia/Seoul")).isAfter(LocalTime.of(8, 0)))
            in = new ClassPathResource("static/image/background_evening.jpg").getInputStream();
        else
            in = new ClassPathResource("static/image/background_night.jpg").getInputStream();
        return IOUtils.toByteArray(in);
    }

    @RequestMapping("get/timetable")
    public String timetable() throws JSONException {
        // https://open.neis.go.kr/hub/hisTimetable?SD_SCHUL_CODE=7530560&ATPT_OFCDC_SC_CODE=J10&GRADE=1&CLASS_NM=3&Type=json&TI_FROM_YMD=20240326&TI_TO_YMD=20240326&KEY=c2faa1eafe12484fae5615db4b90ff4f
        Calendars calendars = new Calendars(Locale.KOREAN);
        String date = calendars.format(calendars.createNow(), "yyyyMMdd");

        String url = "https://open.neis.go.kr/hub/hisTimetable?SD_SCHUL_CODE=7530560&ATPT_OFCDC_SC_CODE=J10&GRADE=1&CLASS_NM=3&Type=json&TI_FROM_YMD="+date+"&TI_TO_YMD="+date+"&KEY="+Constant.NeisApiKey;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> exchange = rt.exchange(url, HttpMethod.GET, entity, String.class);

        return new JSONObject(exchange.getBody()).toString();
    }

    @RequestMapping("user")
    public String userInfo(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(getSessionId(request.getCookies()));
    }


    @RequestMapping(value = "robots.txt")
    public String robots() {
        return "User-agent: *\n" +
                "Allow: /";
    }

    @RequestMapping(value = "privacy")
    public String privacy() {
        return "이 웹 어플리케이션은 사용자의 ";
    }


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
