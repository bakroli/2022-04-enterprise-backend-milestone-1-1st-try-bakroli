package com.codecool.exams.supercraftsman.repository;

import com.codecool.exams.supercraftsman.model.Expertise;

import java.util.List;
import java.util.Map;

public interface ExpertiseDAO {
    List<Expertise> getAll();
    long save(Expertise e);
    Expertise getById(long id);
    void delete(long id);
    void update(long id, Expertise expertise);
    void addCrartsmanToExpertise(long eId, long cId);
    Map<Long, Map<Long, Long>> getECList();
}
