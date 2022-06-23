package com.example.demo.controller.web;

import com.example.demo.dto.BoardDetailDTO;
import com.example.demo.dto.BoardListDTO;
import com.example.demo.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final BoardServiceImpl boardService;

    @GetMapping("/")
    public String home(Model model){

        List<BoardListDTO> board = boardService.getAllDTO();
        model.addAttribute("board", board);

        return "home";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/signin")
    public String signin(){
        return "signin";
    }

    @GetMapping("/new")
    public String newPost(){
        return "new";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Long id, Model model){

        BoardDetailDTO detail = boardService.getDetail(id);
        model.addAttribute("detail", detail);

        return "detail";
    }

}
