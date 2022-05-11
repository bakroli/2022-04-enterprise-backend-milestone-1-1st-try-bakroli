package com.codecool.exams.supercraftsman.service;

import com.codecool.exams.supercraftsman.model.Craftsman;
import com.codecool.exams.supercraftsman.repository.CraftsmanDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CraftsmanService {

    private CraftsmanDAO<Craftsman> craftsmanDAO;

    @Autowired
    public CraftsmanService(CraftsmanDAO<Craftsman> craftsmanDAO) {
        this.craftsmanDAO = craftsmanDAO;
    }

    public List<Craftsman> getAll() {
        return craftsmanDAO.getAll();
    }

    public Craftsman get(long id) {
        return craftsmanDAO.get(id);
    }

    public long save(Craftsman craftsman) {
        return craftsmanDAO.save(craftsman);
    }

    public void update(long id, Craftsman craftsman) {
        craftsmanDAO.update(id, craftsman);
    }

    public void delete(long id) {
        craftsmanDAO.delete(id);
    }

    public List<String> getByIdByExpertise(long id) {
        return craftsmanDAO.getByIdByExpertise(id);
    }
}
