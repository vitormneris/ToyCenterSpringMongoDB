package br.edu.toycenter.controller.web;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/administrator")
public class TeamWebController {


    @GetMapping("/team")
    public String equipe(Model model) {
        return "/team";
    }
}
