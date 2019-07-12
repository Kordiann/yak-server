package com.example.demo.Repositories;

import com.example.demo.Models.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    List<Team> findAll();
}
