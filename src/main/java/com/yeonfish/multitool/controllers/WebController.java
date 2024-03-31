package com.yeonfish.multitool.controllers;

import com.yeonfish.multitool.beans.vo.AlimiVO;
import com.yeonfish.multitool.devController.logger;
import com.yeonfish.multitool.services.AlimManageService;
import com.yeonfish.multitool.util.UUID;
import jakarta.servlet.http.HttpServletResponse;
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

    @RequestMapping("/new")
    public String newA() {
        return "new";
    }

    @RequestMapping("/seat_change")
    public String seatChange() {
        return "seat_change";
    }

    @RequestMapping("/")
    public String index(Model model) {
//        log.info(alimManageService.getAlim());
        model.addAttribute("alim", alimManageService.getAlim());
        return "new";
    }
}
