package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeloshopDaoImpl implements VeloshopDao {
    String url = "jdbc:mysql://" + host + ":" + port + "/VeloShop?allowPublicKeyRetrieval=true&useSSL=false";

    @Override
    public StorageItem selectById(int id) {
        try {
            // Verbindung aufbauen
            Connection conn = DriverManager.getConnection(url, mysqlUser, mysqlPassword);

            // SQL-Befehl vorbereiten
            String query = "SELECT * FROM StorageItems where itemId = " + id + ";";

            // Statement erstellen
            Statement stmt = conn.createStatement();

            // Abfrage ausführen
            ResultSet rs = stmt.executeQuery(query);

            // Ergebnisse ausgeben
            List<StorageItem> storageItems = new ArrayList<>();
            while (rs.next()) {
                StorageItem storageItem = new StorageItem(rs.getInt("itemId"), rs.getString("type"), rs.getInt("amount"), rs.getDouble("price"));
                storageItems.add(storageItem);
            }

            // Ressourcen schließen
            rs.close();
            stmt.close();
            conn.close();
            return storageItems.getFirst();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StorageItem> selectAll() {
        try {
            // Verbindung aufbauen
            Connection conn = DriverManager.getConnection(url, mysqlUser, mysqlPassword);

            // SQL-Befehl vorbereiten
            String query = "SELECT * FROM StorageItems;";

            // Statement erstellen
            Statement stmt = conn.createStatement();

            // Abfrage ausführen
            ResultSet rs = stmt.executeQuery(query);

            // Ergebnisse ausgeben
            List<StorageItem> storageItems = new ArrayList<>();
            while (rs.next()) {
                StorageItem storageItem = new StorageItem(rs.getInt("itemId"), rs.getString("type"), rs.getInt("amount"), rs.getDouble("price"));
                storageItems.add(storageItem);
            }

            // Ressourcen schließen
            rs.close();
            stmt.close();
            conn.close();
            return storageItems;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StorageItem save(StorageItem storageItem) {
        try {
            // Verbindung aufbauen
            Connection conn = DriverManager.getConnection(url, mysqlUser, mysqlPassword);

            // SQL-Befehl vorbereiten
            String sql = "INSERT INTO VeloShop.StorageItems (itemId, type, amount, price) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, storageItem.getItemId());
                stmt.setString(2, storageItem.getType());
                stmt.setInt(3, storageItem.getAmount());
                stmt.setDouble(4, storageItem.getPrice());

                int rowsInserted = stmt.executeUpdate();

                // Ressourcen schließen
                stmt.close();
                conn.close();
                if (rowsInserted > 0) {
                    return selectById(storageItem.getItemId());
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StorageItem update(StorageItem storageItem) {
        try (Connection conn = DriverManager.getConnection(url, mysqlUser, mysqlPassword)) {

            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);

            String existQuery = "SELECT COUNT(*) FROM VeloShop.StorageItems WHERE itemId = ?";
            try (PreparedStatement existStmt = conn.prepareStatement(existQuery)) {
                existStmt.setInt(1, storageItem.getItemId());
                try (ResultSet rs = existStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new RuntimeException("The product with this id does not exist");
                    }
                }
            }

            int currentAmount = 0;

            String checkQuery = "SELECT amount FROM VeloShop.StorageItems WHERE itemId = ? FOR UPDATE";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, storageItem.getItemId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        currentAmount = rs.getInt("amount");
                    }
                }
            }

            if (currentAmount < storageItem.getAmount()) {
                conn.rollback();
                throw new RuntimeException("Lagerbestand zu klein!");
            }

            int newAmount = storageItem.getAmount();

            String sql = "UPDATE VeloShop.StorageItems SET amount = ? WHERE itemId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, newAmount);
                stmt.setInt(2, storageItem.getItemId());
                int rowsUpdated = stmt.executeUpdate();

                conn.commit();

                if (rowsUpdated > 0) {
                    return selectById(storageItem.getItemId());
                }
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(int id) {
        try {
            // Verbindung aufbauen
            Connection conn = DriverManager.getConnection(url, mysqlUser, mysqlPassword);

            // SQL-Befehl vorbereiten
            String sql = "DELETE FROM VeloShop.StorageItems WHERE itemId = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
