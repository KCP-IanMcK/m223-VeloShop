package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeloshopDaoImpl implements VeloshopDao {
    String url = host + ":" + port;

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
        try {
            // Verbindung aufbauen
            Connection conn = DriverManager.getConnection(url, mysqlUser, mysqlPassword);

            // SQL-Befehl vorbereiten
            String sql = "UPDATE VeloShop.StorageItems SET type = ?, amount = ?, price = ? WHERE itemId = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, storageItem.getType());
                stmt.setInt(2, storageItem.getAmount());
                stmt.setDouble(3, storageItem.getPrice());
                stmt.setInt(4, storageItem.getItemId());

                int rowsUpdated = stmt.executeUpdate();

                stmt.close();
                conn.close();

                if (rowsUpdated > 0) {
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
