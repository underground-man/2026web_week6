package org.example.webui.week6_sb_crud_web.controller;


import org.example.webui.week6_sb_crud_web.dto.MovieRequest;
import org.example.webui.week6_sb_crud_web.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/movies")
public class MovieWebController {

    private MovieService movieService;
    public  MovieWebController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping
    public String list (Model model){
        model.addAttribute("movies",movieService.findAll());
        return "movies/list";
    }

    @GetMapping("/new")
    public String form(){
        return "movies/form";
    }

    @PostMapping
    public String create(@ModelAttribute MovieRequest request){
        movieService.create(request);

        return "redirect:/movies";
    }




}
