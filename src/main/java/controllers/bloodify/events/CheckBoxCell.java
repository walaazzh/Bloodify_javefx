package controllers.bloodify.events;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
public class CheckBoxCell <T> extends TableCell<T, Boolean>{



    private final CheckBox checkBox;

    public CheckBoxCell() {
        this.checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            T item = getTableView().getItems().get(getIndex());
            getTableView().getSelectionModel().select(item);
        });
        setGraphic(checkBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(checkBox);
            if (item != null) {
                checkBox.setSelected(item);
            }
        }
    }
}

