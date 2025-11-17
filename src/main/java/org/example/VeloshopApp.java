package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class VeloshopApp extends Application {

    private final VeloshopDao dao = new VeloshopDaoImpl();
    private TableView<StorageItem> table;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VeloShop Storage");

        // Tabelle
        table = new TableView<>();
        TableColumn<StorageItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getItemId()).asObject());

        TableColumn<StorageItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        TableColumn<StorageItem, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getAmount()).asObject());

        TableColumn<StorageItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        table.getColumns().addAll(idCol, typeCol, amountCol, priceCol);
        refreshTable();

        // Eingabefelder
        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField typeField = new TextField(); typeField.setPromptText("Type");
        TextField amountField = new TextField(); amountField.setPromptText("Amount");
        TextField priceField = new TextField(); priceField.setPromptText("Price");

        Button addBtn = new Button("Save");
        addBtn.setOnAction(e -> {
            try {
                StorageItem item = new StorageItem(
                        Integer.parseInt(idField.getText()),
                        typeField.getText(),
                        Integer.parseInt(amountField.getText()),
                        Double.parseDouble(priceField.getText())
                );
                dao.save(item);
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError(ex);
            }
        });

        Button updateBtn = new Button("Update Amount");
        updateBtn.setOnAction(e -> {
            try {
                StorageItem item = new StorageItem(
                        Integer.parseInt(idField.getText()),
                        typeField.getText(),
                        Integer.parseInt(amountField.getText()),
                        Double.parseDouble(priceField.getText())
                );
                dao.update(item);
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError(ex);
            }
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            try {
                dao.delete(Integer.parseInt(idField.getText()));
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                showError(ex);
            }
        });

        HBox inputBox = new HBox(10, idField, typeField, amountField, priceField, addBtn, updateBtn, deleteBtn);
        inputBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, inputBox);
        layout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout, 900, 500));
        primaryStage.show();
    }

    private void refreshTable() {
        try {
            List<StorageItem> items = dao.selectAll();
            if (items != null) {
                table.getItems().setAll(items);
            } else {
                table.getItems().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(e);
        }
    }

    private void showError(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
