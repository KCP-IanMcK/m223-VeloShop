package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class VeloshopDaoImplTest {

    VeloshopDao veloshopDao = new VeloshopDaoImpl();
    StorageItem storageItemSetUp = new StorageItem(1, "Rad", 10, 120.00);

    @BeforeEach
    void setUp() {
        veloshopDao.save(storageItemSetUp);
    }

    @AfterEach
    void tearDown() {
        List<StorageItem> storageItems = veloshopDao.selectAll();

        for (StorageItem storageItem : storageItems) {
            veloshopDao.delete(storageItem.getItemId());
        }
    }

    @Test
    void selectById() {
        StorageItem storageItem = veloshopDao.selectById(1);

        Assertions.assertEquals(storageItem.getType(), storageItemSetUp.getType());
        Assertions.assertEquals(storageItem.getPrice(), storageItemSetUp.getPrice());
        Assertions.assertEquals(storageItem.getAmount(), storageItemSetUp.getAmount());
    }

    @Test
    void selectAll() {
        List<StorageItem> storageItems = veloshopDao.selectAll();
        Assertions.assertEquals(1, storageItems.size());
    }

    @Test
    void save() {
        StorageItem storageItemToSave = new StorageItem(2, "Rahmen", 10, 120.00);
        StorageItem storageItem = veloshopDao.save(storageItemToSave);
        Assertions.assertEquals(storageItem.getType(), storageItemToSave.getType());
        Assertions.assertEquals(storageItem.getAmount(), storageItemToSave.getAmount());
        Assertions.assertEquals(storageItem.getPrice(), storageItemToSave.getPrice());
    }

    @Test
    void update() {
        StorageItem storageItemToUpdateTo = new StorageItem(1, "Rad", 8, 120.00);
        StorageItem storageItem = veloshopDao.update(storageItemToUpdateTo);
        Assertions.assertEquals(storageItem.getType(), storageItemToUpdateTo.getType());
        Assertions.assertEquals(storageItem.getAmount(), storageItemToUpdateTo.getAmount());
        Assertions.assertEquals(storageItem.getPrice(), storageItemToUpdateTo.getPrice());
    }

    @Test
    void delete() {
        veloshopDao.delete(storageItemSetUp.getItemId());
        List<StorageItem> storageItems = veloshopDao.selectAll();
        Assertions.assertTrue(storageItems.isEmpty());
    }
}