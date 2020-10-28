package com.clubtaekwondo.club.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/coach")
public class CoachController {

    @GetMapping(value = "/coachList")
    public String coachList(){

        return ("adminPart/coach/coachList");
    }
}
