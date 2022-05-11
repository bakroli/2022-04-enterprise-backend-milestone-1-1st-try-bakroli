package com.codecool.exams.supercraftsman.controller;

import com.codecool.exams.supercraftsman.model.Craftsman;
import com.codecool.exams.supercraftsman.model.Expertise;
import com.codecool.exams.supercraftsman.service.ExpertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expertise")
public class ExpertiseController {

    private ExpertiseService expertiseService;

    @Autowired
    public ExpertiseController(ExpertiseService expertiseService) {
        this.expertiseService = expertiseService;
    }

    @GetMapping
    public List<Expertise> getAll() {
        return expertiseService.getAll();
    }

    @PostMapping
    public long save(@RequestBody Expertise e) {
        return expertiseService.save(e);
    }

    @GetMapping("/{id}")
    public Expertise getById(@PathVariable long id) {
        return expertiseService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        expertiseService.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") long id, @RequestBody Expertise expertise) {
        expertiseService.update(id, expertise);
    }

    @PutMapping("/{id}/{c_id}")
    public void addCrartsmanToExpertise(@PathVariable("id") long eId, @PathVariable("c_id") long cId) {
        expertiseService.addCrartsmanToExpertise(eId, cId);
    }

    @GetMapping("/{id}/craftsmen")
    public List<Craftsman> getByExpertiseIdforCraftsmen(@PathVariable("id") long id) {
        return expertiseService.getByExpertiseIdforCraftsmen(id);
    }
}
