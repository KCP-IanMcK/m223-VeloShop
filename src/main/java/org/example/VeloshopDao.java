package org.example;

import java.util.List;

public interface VeloshopDao extends Dao {
    Veloshop selectById(int id);
    List<Veloshop> selectAll();
    Veloshop save(Veloshop veloshop);
    Veloshop update(Veloshop veloshop);
    void delete(int id);
}
