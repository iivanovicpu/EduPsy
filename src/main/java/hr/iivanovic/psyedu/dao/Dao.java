package hr.iivanovic.psyedu.dao;

import java.util.List;

import hr.iivanovic.psyedu.entity.Entity;

public interface Dao<T extends Entity, I> {

    List<T> findAll();


    T find(I id);


    T save(T newsEntry);


    void delete(I id);

}