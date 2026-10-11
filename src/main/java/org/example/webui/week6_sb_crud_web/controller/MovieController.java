package org.example.webui.week6_sb_crud_web.controller;

import org.example.webui.week6_sb_crud_web.*;
import org.example.webui.week6_sb_crud_web.dto.MovieRequest;
import org.example.webui.week6_sb_crud_web.dto.MovieResponse;
import org.example.webui.week6_sb_crud_web.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

        public MovieController(MovieService movieService){
            this.movieService =movieService;
        }

        @PostMapping
        public ResponseEntity<MovieResponse> create(@RequestBody MovieRequest request){
            return ResponseEntity.status(HttpStatus.CREATED).body(movieService.create(request));
        }


        @GetMapping
        public List<MovieResponse> findAll(){
            return movieService.findAll();
        }

        @GetMapping("/{id}")
        public MovieResponse findbyid(@PathVariable Long id){
            return movieService.findbyId(id);
        }

        @PutMapping("/{id}")
        public MovieResponse update(@PathVariable Long id, @RequestBody MovieRequest m){
            return movieService.update(id,m);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id){
            movieService.delete(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/category")
        public List<MovieResponse> findCat(@RequestParam String category){
            return movieService.findCat(category);

        }
}
