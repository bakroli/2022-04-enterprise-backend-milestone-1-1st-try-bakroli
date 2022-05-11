package com.codecool.exams.supercraftsman.service;

import com.codecool.exams.supercraftsman.model.Craftsman;
import com.codecool.exams.supercraftsman.model.Expertise;
import com.codecool.exams.supercraftsman.repository.CraftsmanDAO;
import com.codecool.exams.supercraftsman.repository.ExpertiseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExpertiseService {

    private ExpertiseDAO expertiseDAO;
    private CraftsmanDAO<Craftsman> craftsmanDAO;

    @Autowired
    public ExpertiseService(ExpertiseDAO expertiseDAO, CraftsmanDAO<Craftsman> craftsmanDAO) {
        this.expertiseDAO = expertiseDAO;
        this.craftsmanDAO = craftsmanDAO;
    }

    public List<Expertise> getAll() {
        return expertiseDAO.getAll();
    }

    public long save(Expertise e) {
        return expertiseDAO.save(e);
    }

    public void delete(long id) {
        expertiseDAO.delete(id);
    }

    public Expertise getById(long id) {
        return expertiseDAO.getById(id);
    }

    public void update(long id, Expertise expertise) {
        expertiseDAO.update(id, expertise);
    }

    public void addCrartsmanToExpertise(long eId, long cId) {
        expertiseDAO.addCrartsmanToExpertise(eId, cId);
    }

    public List<Craftsman> getByExpertiseIdforCraftsmen(long id) {
       for (Map<Long, Long> e : expertiseDAO.getECList().values()) {
       }
       return new ArrayList<>();
}
