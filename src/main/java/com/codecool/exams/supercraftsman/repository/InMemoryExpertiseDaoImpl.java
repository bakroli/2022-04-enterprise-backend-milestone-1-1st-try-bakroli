package com.codecool.exams.supercraftsman.repository;

import com.codecool.exams.supercraftsman.model.Expertise;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryExpertiseDaoImpl implements ExpertiseDAO{

    private List<Expertise> expertiseList = new ArrayList<>();
    private Map<Long, Map<Long, Long>> eCList = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong();
    private AtomicLong idGeneratorECList = new AtomicLong();


    public void clear() {
        expertiseList = new ArrayList<>();
        eCList = new HashMap<>();
    }

    @Override
    public List<Expertise> getAll() {
        return new ArrayList<>(expertiseList);
    }

    @Override
    public long save(Expertise e) {
        long id = idGenerator.incrementAndGet();
        e.setId(id);
        expertiseList.add(e);
        return id;
    }

    @Override
    public Expertise getById(long id) {
        return expertiseList.stream().filter(i->i.getId()==id).findFirst().orElseThrow();
    }

    @Override
    public void delete(long id) {
        Expertise deleteExpertise = null;
        for (Expertise expertise : expertiseList) {
            if (expertise.getId() == id) {
                deleteExpertise = expertise;
                break;
            }
        }
        if (deleteExpertise != null) {
            expertiseList.remove(deleteExpertise);
        }
    }

    @Override
    public void update(long id, Expertise expertise) {
        for (Expertise e : expertiseList) {
            if (e.getId() == id) {
                e.setName(expertise.getName());
                break;
            }
        }
    }

    @Override
    public void addCrartsmanToExpertise(long eId, long cId) {
        long id = idGeneratorECList.incrementAndGet();
        Map<Long, Long> newCE = new HashMap<>();
        newCE.put(eId, cId);
        eCList.put(id, newCE);
    }

    public Map<Long, Map<Long, Long>> getECList() {
        return new HashMap<>(eCList);
    }


}
