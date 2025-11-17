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
        primaryStage.setTitle("VeloShop - Kundenbestellung");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StorageItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getItemId()).asObject());

        TableColumn<StorageItem, String> typeCol = new TableColumn<>("Artikel");
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        TableColumn<StorageItem, Integer> amountCol = new TableColumn<>("Verfügbar");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getAmount()).asObject());

        TableColumn<StorageItem, Double> priceCol = new TableColumn<>("Preis");
        priceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        table.getColumns().addAll(idCol, typeCol, amountCol, priceCol);
        refreshTable();

        // -------------------------------
        // Eingabefelder für Bestellung
        // -------------------------------
        TextField itemIdField = new TextField();
        itemIdField.setPromptText("Artikel-ID");
        itemIdField.setEditable(false);

        TextField typeField = new TextField();
        typeField.setPromptText("Artikelname");
        typeField.setEditable(false);

        TextField orderAmountField = new TextField();
        orderAmountField.setPromptText("Bestellmenge");

        // Füllt Felder automatisch, wenn Kunde etwas in der Tabelle auswählt
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                itemIdField.setText(String.valueOf(newSel.getItemId()));
                typeField.setText(newSel.getType());
            }
        });

        // -------------------------------
        // Bestellen Button
        // -------------------------------
        Button orderBtn = new Button("Bestellen");
        orderBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(itemIdField.getText());
                int amount = Integer.parseInt(orderAmountField.getText());

                if (amount <= 0) {
                    showError(new RuntimeException("Menge muss > 0 sein"));
                    return;
                }

                StorageItem existing = dao.selectById(id);
                if (existing == null) {
                    showError(new RuntimeException("Artikel existiert nicht."));
                    return;
                }

                // Neue Menge für DB
                int newAmount = existing.getAmount() - amount;

                StorageItem updated = new StorageItem(id, existing.getType(), newAmount, existing.getPrice());
                dao.update(updated); // verwendet deine Transaktionslogik

                refreshTable();
                orderAmountField.clear();

            } catch (Exception ex) {
                ex.printStackTrace();
                showError(ex);
            }
        });

        // -------------------------------
        // Refresh Button
        // -------------------------------
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshTable());

        HBox orderBox = new HBox(10, itemIdField, typeField, orderAmountField, orderBtn, refreshBtn);
        orderBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, table, orderBox);
        layout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout, 900, 500));
        primaryStage.show();
    }

    private void refreshTable() {
        try {
            List<StorageItem> items = dao.selectAll();
            table.getItems().setAll(items);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Es ist ein Problem aufgetreten");
        alert.setContentText(ex.getMessage());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
