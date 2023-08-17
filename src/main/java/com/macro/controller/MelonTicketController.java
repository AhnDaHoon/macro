package com.macro.controller;

import com.macro.dto.MelonInfo;
import com.macro.dto.MelonUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/melon")
public class MelonTicketController {

    @GetMapping("")
    public String melon(){
        return "view/melon/index";
    }

    @PostMapping("/login")
    @ResponseBody
    public String melonLogin(@RequestBody MelonUser melonUser){

        System.out.println("melonUser = " + melonUser);
        return null;
    }
}
