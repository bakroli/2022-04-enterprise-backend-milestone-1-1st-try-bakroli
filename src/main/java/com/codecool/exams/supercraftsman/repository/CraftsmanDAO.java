package com.codecool.exams.supercraftsman.repository;

import java.util.List;

public interface CraftsmanDAO<T> {
    List<T> getAll();
    T get(long id);
    long save(T t);
    void update(long id, T t);
    void delete(long id);
    List<String> getByIdByExpertise(long id);
}
