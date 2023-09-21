package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presenters.Presenter;

public class ContainerView implements View{

    Presenter presenter;
    View currentView;

    private Label firstname = new Label();
    private Label lastname = new Label();
    private Label names = new Label();

    private HBox namesBox = new HBox();{
        namesBox.getChildren().add(names);
    }

    private VBox header = new VBox();{

        header.getChildren().add(namesBox);
        namesBox.setAlignment(Pos.BASELINE_RIGHT);

        header.getStyleClass().add("header");
    }

    private BorderPane mainPane = new BorderPane();{
        mainPane.setTop(namesBox);
    }

    public void setHeader(String firstname, String lastname){
        names.setText(firstname + " " + lastname);
    }

    public void changeView(View v, Presenter p){
        this.currentView = v;
        this.currentView.setPresenter(p);
        mainPane.setCenter(currentView.getRoot());
    }

    public void setStatus(String msg){
        header.getChildren().add(new Label(msg));
    }

    @Override
    public void setPresenter(Presenter p) { this.presenter = p; }

    @Override
    public Parent getRoot() { return mainPane;}
}