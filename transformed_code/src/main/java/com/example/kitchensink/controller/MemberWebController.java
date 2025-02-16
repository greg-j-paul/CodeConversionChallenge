package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("kitchensink")
public class MemberWebController {

    @Autowired
    private MemberService memberService;

    @GetMapping({"/index", "/"})
    public String showIndexPage(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("newmember", new Member());
        return "index";
    }

    @PostMapping("/index")
    public String registerMember(@Valid @ModelAttribute("newmember") Member member, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Error while registering user");
            model.addAttribute("members", memberService.getAllMembers()); // Ensures that the members list is still loaded

            return "index";
        }

        try {
            memberService.register(member);
            model.addAttribute("message", "Succesfully registered user");
            return "redirect:index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("members", memberService.getAllMembers()); // Ensures that the members list is still loaded

            return "index";
        }
    }

}
