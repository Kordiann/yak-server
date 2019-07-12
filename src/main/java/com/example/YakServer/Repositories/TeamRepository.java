package com.example.YakServer.Repositories;

import com.example.YakServer.Models.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    List<Team> findAll();
}
