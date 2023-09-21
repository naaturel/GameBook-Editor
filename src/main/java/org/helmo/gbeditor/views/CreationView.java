package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presenters.Presenter;
import org.helmo.gbeditor.presenters.CreationPresenter;
import org.helmo.gbeditor.presenters.ViewInterfaces.CreationViewInterface;

public class CreationView implements View, CreationViewInterface {

    CreationPresenter creationPresenter;

    private TextField regNumb = new TextField("Matricule");{
        regNumb.getStyleClass().add("isbn");
    }

    private TextField title = new TextField("Title");{
        title.getStyleClass().add("title");
    }

    private TextArea summary = new TextArea("Your summary here...");{
        summary.getStyleClass().add("summary");
    }

    private Label writtingCheck = new Label();{
        writtingCheck.getStyleClass().add("writtingCheck");
    }

    private VBox bookEditor = new VBox();{

        Button save = new Button("Enregistrer");{
            save.setOnAction(action -> creationPresenter.clickOnSave(
                    regNumb.getText(), title.getText(), summary.getText(),
                    creationPresenter.getNames(1), creationPresenter.getNames(2)));
        }

        Button edit = new Button("Editer un livre");{
            edit.setOnAction(action ->
                    creationPresenter.goTo(Views.EDITION));
        }

        bookEditor.getStyleClass().add("editor");

        bookEditor.getChildren().add(regNumb);
        bookEditor.getChildren().add(title);
        bookEditor.getChildren().add(summary);
        bookEditor.getChildren().add(save);
        bookEditor.getChildren().add(edit);
        bookEditor.getChildren().add(writtingCheck);
    }

    private BorderPane mainPane = new BorderPane();{
        mainPane.setCenter(bookEditor);

    }

    @Override
    public void setStatus(String msg, int type) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        switch(type){
            case 0:
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("(┛◉Д◉)┛彡┻━┻");
                break;
            case 1:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("(｡◕‿‿◕｡)");
                break;
        }
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    @Override
    public void setPresenter(Presenter p) {
        this.creationPresenter = (CreationPresenter)p;
    }

    @Override
    public Parent getRoot() { return mainPane; }
}
