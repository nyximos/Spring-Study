package com.example.demo.controller.web;

import com.example.demo.domain.Board;
import com.example.demo.dto.BoardListDTO;
import com.example.demo.repository.BoardRepository;
import com.example.demo.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberWebController {

    private final BoardRepository boardRepository;
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

}
