package org.example;

import java.util.List;

public interface VeloshopDao extends Dao {
    StorageItem selectById(int id);
    List<StorageItem> selectAll();
    StorageItem save(StorageItem storageItem);
    StorageItem update(StorageItem storageItem);
    void delete(int id);
}
