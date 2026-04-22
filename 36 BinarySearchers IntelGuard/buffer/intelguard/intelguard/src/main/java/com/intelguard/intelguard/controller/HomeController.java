// controller/HomeController.java
package com.intelguard.intelguard.controller;

import com.intelguard.intelguard.model.ThreatResult;
import com.intelguard.intelguard.service.ThreatAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    ThreatAnalyzerService service;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/scan")
    public String scan(@RequestParam String agent,
                       @RequestParam String prompt,
                       Model model) {

        ThreatResult result = service.analyze(agent,prompt);

        model.addAttribute("result", result);

        return "result";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("list", service.getLogs());

        return "dashboard";
    }
}