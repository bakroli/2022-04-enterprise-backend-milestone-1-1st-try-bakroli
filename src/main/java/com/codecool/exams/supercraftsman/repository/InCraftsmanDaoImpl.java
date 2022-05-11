package com.codecool.exams.supercraftsman.repository;

import com.codecool.exams.supercraftsman.model.Craftsman;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InCraftsmanDaoImpl implements CraftsmanDAO<Craftsman> {

    private List<Craftsman> craftsmen = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong();

    public void clear() {
        craftsmen = new ArrayList<>();
    }

    @Override
    public List<Craftsman> getAll() {
        return new ArrayList<>(craftsmen);
    }

    @Override
    public Craftsman get(long id) {
        return craftsmen.stream().filter(i->i.getId()==id).findFirst().orElseThrow();
    }

    @Override
    public long save(Craftsman craftsman) {
        long id = idGenerator.incrementAndGet();
        craftsman.setId(id);
        craftsmen.add(craftsman);
        return id;
    }

    @Override
    public void update(long id, Craftsman craftsman) {
        for (Craftsman  c : craftsmen) {
            if (c.getId() == id) {
                c.setName(craftsman.getName());
                c.setEmail(craftsman.getEmail());
                c.setPhoneNumber(craftsman.getPhoneNumber());
                break;
            }
        }
    }

    @Override
    public void delete(long id) {
        Craftsman deleteCrastman = null;
        for (Craftsman craftsman : craftsmen) {
            if (craftsman.getId() == id) {
                deleteCrastman = craftsman;
                break;
            }
        }
        if (deleteCrastman != null) {
            craftsmen.remove(deleteCrastman);
        }
    }

    @Override
    public List<String> getByIdByExpertise(long id) {
        return null;
    }
}
