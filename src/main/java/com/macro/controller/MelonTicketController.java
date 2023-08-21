package com.macro.controller;
import com.macro.config.LoginTypeEnum;
import com.macro.dto.MelonInfo;
import com.macro.dto.MelonUser;
import com.macro.service.MelonTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/melon")
public class MelonTicketController {

    @Autowired
    private MelonTicketService melonTicketService;

    @GetMapping("")
    public String melon(){
        return "view/melon/index";
    }

    @PostMapping("/login")
    @ResponseBody
    public String melonLogin(@ModelAttribute MelonInfo melonInfo){
        melonInfo.setLoginType(LoginTypeEnum.MELON);

        melonTicketService.melonLogin(melonInfo);
        return "로그인 성공";
    }

    @PostMapping("/ticketing")
    @ResponseBody
    public String melonTicketing(@ModelAttribute MelonInfo melonInfo){
        melonInfo.setLoginType(LoginTypeEnum.MELON);
        System.out.println("melonInfo = " + melonInfo);

//        melonTicketService.melonLogin(melonInfo);
        return "티켓팅 성공";
    }
}
