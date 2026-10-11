package org.example.webui.week6_sb_crud_web.repository;

import org.example.webui.week6_sb_crud_web.domain.Movie;

import java.util.List;
import java.util.Optional;


public interface MovieRepository {
    Movie save(Movie b);
    List<Movie> findAll();
    Optional<Movie> findById(Long Id);
    Movie update(Movie b);
    void delete(long id);

}
