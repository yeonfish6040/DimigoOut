package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.beans.dao.JokeDAO;
import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.services.AlimManageService;
import com.yeonfish.multitool.util.UUID;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Controller
@RequestMapping("/")
public class WebController {

    private final logger log = new logger();

    @Autowired
    AlimManageService alimManageService;

    @Autowired
    JokeDAO jokeDAO;

    @RequestMapping("/seat_change")
    public String seatChange() {
        return "seat_change";
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) throws JSONException {
//        log.info(alimManageService.getAlim());

        String something = getSessionId(request.getCookies());
        String id = (new JSONObject((String) (request.getSession().getAttribute(something)))).getString("id");
        if (jokeDAO.getJoke(id) == null || jokeDAO.getJoke(id).equals("")) {
            return "joke";
        }
        model.addAttribute("alim", alimManageService.getAlim());
        return "new";
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
