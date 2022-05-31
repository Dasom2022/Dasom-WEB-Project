package com.dama.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SensorController {
    @GetMapping("/view")
    public String viewHtml(){
        return "sensor/view";
    }
}
