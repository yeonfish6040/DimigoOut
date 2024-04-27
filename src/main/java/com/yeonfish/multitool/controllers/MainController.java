package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.Constant;
import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.beans.vo.StatusVO;
import com.yeonfish.multitool.beans.vo.UserVO;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.services.AlimManageService;
import com.yeonfish.multitool.services.StatusManageService;
import com.yeonfish.multitool.services.UserManageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.expression.Calendars;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;


@RestController
@RequestMapping("/")
public class MainController {
    private final logger log = new logger();

    @Autowired
    private AlimManageService alimManageService;

    @Autowired
    private StatusManageService statusManageService;

    @Autowired
    private UserManageService userManageService;

    @RequestMapping("/public/auth")
    public String auth(@RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
        HttpEntity<String> entity = new HttpEntity<>("", new HttpHeaders());
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> exchange = rt.exchange(
                "https://auth.dimigo.net/oauth/public",
                HttpMethod.GET,
                entity,
                String.class
        );
        String public_key = exchange.getBody();
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String decodedJWT = new String(decoder.decode(token.split("[.]")[1]));

        String sessionId = getSessionId(request.getCookies());

        JSONObject user = (new JSONObject(decodedJWT)).getJSONObject("data");
        UserVO userVO = new UserVO(); userVO.setId(user.getString("id")); userVO.setName(user.getString("name")); userVO.setNumber(user.getString("number")); userVO.setGender(user.getString("gender")); userVO.setType(user.getString("type")); userVO.setProfile_image(user.getString("profile_image"));
        userVO.setSession(sessionId);

        userManageService.setUser(userVO);

        response.sendRedirect("/");
        return "success";
    }

    @RequestMapping("/public/schedule")
    public String schedule(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
        JSONArray weekday = new JSONArray();
        weekday.put(scheduleMaker("wakeup", "0630", "0720", "기상 및 인원점검"));
        weekday.put(scheduleMaker("breakfast", "0720", "0815", "아침식사"));
        weekday.put(scheduleMaker("morningProgram", "0815", "0850", "아침 프로그램"));
        weekday.put(scheduleMaker("prepareForLecture", "0850", "0900", "조회 및 수업준비"));
        weekday.put(scheduleMaker("perio_1", "0900", "0950", "1교시"));
        weekday.put(scheduleMaker("rest", "0950", "1000", "쉬는시간"));
        weekday.put(scheduleMaker("perio_2", "1000", "1050", "2교시"));
        weekday.put(scheduleMaker("rest", "1050", "1100", "쉬는시간"));
        weekday.put(scheduleMaker("perio_3", "1100", "1150", "3교시"));
        weekday.put(scheduleMaker("rest", "1150", "1200", "쉬는시간"));
        weekday.put(scheduleMaker("perio_4", "1200", "1250", "4교시"));
        weekday.put(scheduleMaker("launch", "1250", "1350", "점심시간"));
        weekday.put(scheduleMaker("perio_5", "1350", "1440", "5교시"));
        weekday.put(scheduleMaker("rest", "1440", "1450", "쉬는시간"));
        weekday.put(scheduleMaker("perio_6", "1450", "1540", "6교시"));
        weekday.put(scheduleMaker("rest", "1540", "1550", "쉬는시간"));
        weekday.put(scheduleMaker("perio_7", "1550", "1640", "7교시"));
        weekday.put(scheduleMaker("afterSchoolPrepare", "1640", "1710", "종례, 청소 및 방과후 수업 준비"));
        weekday.put(scheduleMaker("afterSchool_study-1", "1710", "1750", "방과후 수업 1타임"));
        weekday.put(scheduleMaker("afterSchool_study-rest", "1750", "1755", "방과후 수업 쉬는 시간"));
        weekday.put(scheduleMaker("afterSchool_study-2", "1755", "1835", "방과후 수업 2타임"));
        weekday.put(scheduleMaker("dinner", "1830", "1950", "저녁식사"));
        weekday.put(scheduleMaker("self_study-1", "1950", "2110", "야자 1타임"));
        weekday.put(scheduleMaker("self_study-rest", "2110", "2130", "야자 쉬는시간"));
        weekday.put(scheduleMaker("self_study-2", "2130", "2250", "야자 2타임"));
        weekday.put(scheduleMaker("hakbonggwan_prepare", "2250", "2350", "샤워 및 취침준비 | 심야자습 이동"));
        weekday.put(scheduleMaker("hakbonggwan_study1-1", "2350", "2450", "취침 | 심야자습 1타임"));
        weekday.put(scheduleMaker("hakbonggwan_study1-2", "0000", "0050", "취침 | 심야자습 1타임"));
        weekday.put(scheduleMaker("hakbonggwan_study2", "0050", "0150", "취침 | 심야자습 2타임"));
        weekday.put(scheduleMaker("hakbonggwan_sleep", "0150", "0630", "취침"));

        JSONArray saturday = new JSONArray();
        saturday.put(scheduleMaker("wakeup", "0700", "0750", "기상 및 인원점검"));
        saturday.put(scheduleMaker("breakfast", "0750", "0810", "아침식사"));
        saturday.put(scheduleMaker("fix_you", "0810", "0850", "개인정비"));
        saturday.put(scheduleMaker("personnel_check", "0850", "0900", "인원 점검"));
        saturday.put(scheduleMaker("morning_study-1", "0900", "1020", "오전 자습 1타임"));
        saturday.put(scheduleMaker("morning_study-rest", "1020", "1040", "오전 자습 쉬는시간"));
        saturday.put(scheduleMaker("morning_study-2", "1040", "1200", "오전 자습 2타임"));
        saturday.put(scheduleMaker("launch", "1200", "1400", "점심시간"));
        saturday.put(scheduleMaker("afternoon_study-1", "1400", "1600", "오후 자습 1타임"));
        saturday.put(scheduleMaker("afternoon_study-rest", "1600", "1620", "오후 자습 쉬는시간"));
        saturday.put(scheduleMaker("afternoon_study-2", "1620", "1800", "오후 자습 2타임"));
        saturday.put(scheduleMaker("dinner", "1800", "2000", "저녁식사"));
        saturday.put(scheduleMaker("evening_study-1", "2000", "2220", "야간 자습 타임"));
        saturday.put(scheduleMaker("hakbonggwan_prepare", "2220", "2350", "샤워 및 취침준비 | 심야자습 이동"));
        saturday.put(scheduleMaker("hakbonggwan_study1-1", "2350", "2450", "취침 | 심야자습 1타임"));
        saturday.put(scheduleMaker("hakbonggwan_study1-2", "0000", "0050", "취침 | 심야자습 1타임"));
        saturday.put(scheduleMaker("hakbonggwan_study2", "0050", "0150", "취침 | 심야자습 2타임"));
        saturday.put(scheduleMaker("hakbonggwan_sleep", "0150", "0630", "취침"));

        JSONArray sunday = new JSONArray();
        saturday.put(scheduleMaker("wakeup", "0700", "0750", "기상 및 인원점검"));
        saturday.put(scheduleMaker("breakfast", "0750", "0810", "아침식사"));
        saturday.put(scheduleMaker("fix_you", "0810", "0830", "개인정비"));
        saturday.put(scheduleMaker("personnel_check", "0830", "0900", "인원 점검"));
        saturday.put(scheduleMaker("morning_study-1", "0900", "1020", "오전 자습 1타임"));
        saturday.put(scheduleMaker("morning_study-rest", "1020", "1040", "오전 자습 쉬는시간"));
        saturday.put(scheduleMaker("morning_study-2", "1040", "1200", "오전 자습 2타임"));
        saturday.put(scheduleMaker("launch", "1200", "1400", "점심시간"));
        saturday.put(scheduleMaker("afternoon_study-1", "1400", "1600", "오후 자습 1타임"));
        saturday.put(scheduleMaker("afternoon_study-rest", "1600", "1620", "오후 자습 쉬는시간"));
        saturday.put(scheduleMaker("afternoon_study-2", "1620", "1800", "오후 자습 2타임"));
        saturday.put(scheduleMaker("dinner", "1800", "2000", "저녁식사"));
        saturday.put(scheduleMaker("evening_study-1", "2000", "2220", "야간 자습 타임"));
        saturday.put(scheduleMaker("hakbonggwan_prepare", "2220", "2350", "샤워 및 취침준비 | 심야자습 이동"));
        saturday.put(scheduleMaker("hakbonggwan_study1-1", "2350", "2450", "취침 | 심야자습 1타임"));
        saturday.put(scheduleMaker("hakbonggwan_study1-2", "0000", "0050", "취침 | 심야자습 1타임"));
        saturday.put(scheduleMaker("hakbonggwan_study2", "0050", "0150", "취침 | 심야자습 2타임"));
        saturday.put(scheduleMaker("hakbonggwan_sleep", "0150", "0630", "취침"));

        LocalDate date = LocalDate.now();
        if (date.getDayOfWeek().getValue() < 6)
            return weekday.toString();
        else if (date.getDayOfWeek().getValue() == 6)
            return saturday.toString();
        else
            return sunday.toString();
    }

    @RequestMapping("joke")
    public String joke(@RequestParam("flag") String flag, HttpServletRequest request) {
        // db는 lyj.kr 유저 네임은 joke.
        // 이 서버의 개발자는 너무나도 유저 pw를 짓기 귀찮은 관계로 비밀번호를 무언가를 해싱한 값으로 설정해버렸네요.
        // 멍충멍충

        // 아아 그냥 공개 할란다.
        if (flag.equals("FLAG{9e79cb39-436b-412d-8a60-95d2f8b650d9}")) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping(value = "set/status", method = RequestMethod.POST)
    public boolean setStatus(@RequestParam("status") int status, @RequestParam(name = "reason", required = false) String reason, HttpServletRequest request) {

        UserVO user = getUser(request);
        StatusVO s_user = new StatusVO(); s_user.setTime(); s_user.setId(user.getId()); s_user.setName(user.getName()); s_user.setNumber(user.getNumber()); s_user.setStatus(status); s_user.setReason(reason);

        log.info(reason);

        return statusManageService.updateStatus(s_user);
    }

    @RequestMapping(value = "set/alim", method = RequestMethod.POST)
    public String saveAlim(@RequestParam("text") String text, HttpServletRequest request, HttpServletResponse response) {
        if (!userManageService.isAdmin(getUser(request))){
            response.setStatus(403);
            return "Access Denied";
        }else {
            AlimiVO alim = new AlimiVO();
            Calendars calendars = new Calendars(Locale.KOREAN);
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

//    @RequestMapping(value = "set/otp", method = RequestMethod.POST)
//    public String setOtp(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
//        if (!userManageService.isAdmin(getUser(request))) {
//            response.sendError(403, "Access denied  ");
//            return "Access Denied";
//        }
//
//        int random_int = (int)(Math.random() * (999999 - 100000 + 1) + 100000);
//        if (session.getAttribute("otp") == null) {
//            ArrayList<Integer> otps = new ArrayList<>();
//            otps.add(random_int);
//            session.setAttribute("otp", otps);
//        }else {
//            ArrayList<Integer> otps = (ArrayList<Integer>) session.getAttribute("otp"); otps.add(random_int);
//            session.setAttribute("otp", otps);
//        }
//        return String.valueOf(random_int);
//    }

//    @RequestMapping("get/dimiOutside")
//    public String getDimiOutSide() {
//
//    }

    @RequestMapping(value = "get/status", method = RequestMethod.GET)
    public int getStatus(HttpServletRequest request) {
        StatusVO user = new StatusVO(); user.setId(getUser(request).getId());

        StatusVO[] result = statusManageService.getStatus(user);
        if (result.length == 0) return 0;
        else return result[0].getStatus();
    }

    @RequestMapping(value = "get/statusList", method = RequestMethod.GET)
    public String getStatusList(HttpServletRequest request) {
        StatusVO user = new StatusVO(); user.setNumber(getUser(request).getNumber().substring(0,2));

        StatusVO[] result = statusManageService.getClassStatusList(user);
        String resultStr = "[";
        for (int i=0;i<result.length;i++) {
            resultStr = resultStr + result[i].toString() + ",";
        }
        resultStr = resultStr.substring(0, resultStr.length()-1) + "]";
        return resultStr;
    }

    @RequestMapping(value = "get/background", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage() throws IOException {
        InputStream in = null;
        if (LocalTime.now(ZoneId.of("Asia/Seoul")).isBefore(LocalTime.of(13, 0)) && LocalTime.now(ZoneId.of("Asia/Seoul")).isAfter(LocalTime.of(4, 0)))
            in = new ClassPathResource("static/image/background_morning.jpg").getInputStream();
        else if (LocalTime.now(ZoneId.of("Asia/Seoul")).isBefore(LocalTime.of(16, 0)) && LocalTime.now(ZoneId.of("Asia/Seoul")).isAfter(LocalTime.of(8, 0)))
            in = new ClassPathResource("static/image/background_evening.jpg").getInputStream();
        else
            in = new ClassPathResource("static/image/background_night.jpg").getInputStream();
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "get/timetable", method = RequestMethod.GET)
    public String timetable(HttpServletRequest request) throws JSONException {
        Calendars calendars = new Calendars(Locale.KOREAN);
        String date = calendars.format(calendars.createNow(), "yyyyMMdd");

        String userClass = getUser(request).getNumber();

        String url = "https://open.neis.go.kr/hub/hisTimetable?SD_SCHUL_CODE=7530560&ATPT_OFCDC_SC_CODE=J10&GRADE="+userClass.charAt(0)+"&CLASS_NM="+userClass.charAt(1)+"&Type=json&TI_FROM_YMD="+date+"&TI_TO_YMD="+date+"&KEY="+Constant.NeisApiKey;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> exchange = rt.exchange(url, HttpMethod.GET, entity, String.class);

        return new JSONObject(exchange.getBody()).toString();
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String userInfo(HttpServletRequest request) {
        return getUser(request).toString();
    }


    @RequestMapping(value = "robots.txt")
    public String robots() {
        return "User-agent: *\n" +
                "Allow: /";
    }

    @RequestMapping(value = "privacy")
    public String privacy() {
        return "이 웹 어플리케이션은 사용자의 개인정보를 저 수면 밑에서 가로체, 여기저기 도용하니 쓸거면 쓰고 말거면 말든가.";
    }


    private UserVO getUser(HttpServletRequest request) {
        UserVO tmp = new UserVO();
        tmp.setSession(getSessionId(request.getCookies()));
        return userManageService.getLoggedInUser(tmp);
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

    private static JSONObject scheduleMaker(String id, String from, String to, String name) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("from", from);
        jsonObject.put("to", to);
        jsonObject.put("name", name);
        return jsonObject;
    }
}
