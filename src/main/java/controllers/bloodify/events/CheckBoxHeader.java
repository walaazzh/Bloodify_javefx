package controllers.bloodify.events;


import javafx.beans.property.BooleanProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CheckBoxHeader<T> extends TableCell<T, Boolean> {
    private final CheckBox checkBox;
    private TableColumn<T, Boolean> column;

    public CheckBoxHeader(TableColumn<T, Boolean> column) {
        this.column = column;
        this.checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            //column.getTableView().getItems().forEach(item -> column.getCellObservableValue(item).setValue(checkBox.isSelected()));
            column.getTableView().getItems().forEach(item -> {
                TableColumn<T, Boolean> tableColumn = column;
                ObservableValue<Boolean> observableValue = tableColumn.getCellObservableValue(item);
                BooleanProperty cellValue = (BooleanProperty) observableValue;
                cellValue.set(checkBox.isSelected());
            });

        });
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateCheckBox());
        setGraphic(checkBox);
    }

    private void updateCheckBox() {
        if (column.getTableView().getItems().isEmpty()) {
            checkBox.setSelected(false);
            return;
        }
        boolean allSelected = column.getTableView().getItems().stream()
                .allMatch(item -> column.getCellObservableValue(item).getValue());
        checkBox.setSelected(allSelected);
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        updateCheckBox();
    }

    public static <S> Callback<TableColumn<S, Boolean>, TableCell<S, Boolean>> forTableColumn() {
        return param -> new CheckBoxHeader<>(param);
    }
}

