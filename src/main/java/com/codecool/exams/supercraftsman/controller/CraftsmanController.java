package com.codecool.exams.supercraftsman.controller;

import com.codecool.exams.supercraftsman.model.Craftsman;
import com.codecool.exams.supercraftsman.service.CraftsmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/craftsman")
public class CraftsmanController {

    private CraftsmanService craftsmanService;

    @Autowired
    public CraftsmanController(CraftsmanService craftsmanService) {
        this.craftsmanService = craftsmanService;
    }

    @GetMapping()
    public List<Craftsman> getAll() {
        return craftsmanService.getAll();
    }

    @GetMapping("/{id}")
    public Craftsman get(@PathVariable("id") long id) {
        return craftsmanService.get(id);
    }

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody Craftsman craftsman, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(craftsmanService.save(craftsman));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") long id, @RequestBody Craftsman craftsman) {
        craftsmanService.update(id, craftsman);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        craftsmanService.delete(id);
    }

    @GetMapping("/{id}/expertise")
    public List<String> getByIdByExpertise(@PathVariable("id") long id) {
        return craftsmanService.getByIdByExpertise(id);
    }
}
