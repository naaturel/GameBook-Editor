package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.helmo.gbeditor.presenters.Presenter;
import org.helmo.gbeditor.presenters.StartPresenter;
import org.helmo.gbeditor.presenters.ViewInterfaces.StartViewInterface;

public class StartView implements View, StartViewInterface {

    StartPresenter startPresenter;

    private Label status = new Label();

    private TextField firstname = new TextField("Laurent");{
        firstname.getStyleClass().add("firstname");
    }

    private TextField lastname = new TextField("Crema");{
        lastname.getStyleClass().add("lastname");
    }

    private VBox credentials = new VBox();{
        credentials.getStyleClass().add("credentials");

        Button connect = new Button("Connexion");{
            connect.setOnAction(action -> startPresenter.clickOnConnect(firstname.getText(), lastname.getText()));
        }

        Label descript = new Label("Identifiez vous");{
            descript.getStyleClass().add("descript");
        }
        credentials.getChildren().add(descript);
        credentials.getChildren().add(firstname);
        credentials.getChildren().add(lastname);
        credentials.getChildren().add(connect);
        credentials.getChildren().add(status);
    }

    private BorderPane mainPane = new BorderPane();{

        mainPane.setCenter(credentials);
    }

    @Override
    public void setStatus(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    @Override
    public void setPresenter(Presenter p) { this.startPresenter = (StartPresenter)p; }

    @Override
    public Parent getRoot(){ return mainPane; }

}
