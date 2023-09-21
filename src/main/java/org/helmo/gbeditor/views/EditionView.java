package org.helmo.gbeditor.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presenters.EditionPresenter;
import org.helmo.gbeditor.presenters.Presenter;
import org.helmo.gbeditor.presenters.ViewInterfaces.EditionViewInterface;
import java.util.Optional;


public class EditionView extends EditionViewComponents implements View, EditionViewInterface {

    private EditionPresenter editionPresenter;

    private ObservableList<String> observablePages = FXCollections.observableArrayList();
    private ListView<String> pages = new ListView<>();{
        pages.setItems(observablePages);
        pages.setMinWidth(400);
        pages.setMinHeight(510);
    }

    private ObservableList<String> observableBooks = FXCollections.observableArrayList();
    private ListView<String> books = new ListView<>();{
        books.setItems(observableBooks);
        books.setMinWidth(400);
        books.setMinHeight(510);
    }

    Button confirm = new Button();

    private VBox bookMenu = new VBox();{

        HBox buttons = new HBox();
        {
            Button addBook = new Button("Ajouter un livre");
            {
                addBook.setOnAction(action ->
                        editionPresenter.goTo(Views.CREATION));
            }

            Button select = new Button("Séléctionner");
            {
                select.setOnAction(action ->
                        editionPresenter.clickOnBook(books.getSelectionModel().getSelectedIndex()));
            }

            buttons.getChildren().add(addBook);
            buttons.getChildren().add(select);
        }

        bookMenu.getChildren().add(books);
        bookMenu.getChildren().add(buttons);
    }

    Button confirmEdit = new Button("Editer");{
        confirmEdit.setOnAction(action ->
                editionPresenter.editBook(books.getSelectionModel().getSelectedIndex(),
                        editIsbn.getText(), editTitle.getText(), editSummary.getText()));
        super.bookInfos.getChildren().add(confirmEdit);
    }

    Button deleteBook = new Button("Supprimer");{
        deleteBook.setOnAction(action -> delBook("Vous allez supprimer ce livre."));
        super.bookInfos.getChildren().add(deleteBook);
    }

    Button deletePage = new Button("Supprimer");{
        deletePage.setOnAction(action -> editionPresenter.clickOnDeletePage(books.getSelectionModel().getSelectedIndex(),
                pages.getSelectionModel().getSelectedIndex()));
    }

    private VBox edition = new VBox();{

        edition.getChildren().add(bookInfos);
        edition.getChildren().add(super.pageInfos);
        super.pageInfos.getChildren().add(confirm);
        super.pageInfos.getChildren().add(deletePage);
        super.pageInfos.setVisible(false);
        edition.getStyleClass().add("edition");
    }

    private VBox pageMenu = new VBox();{

        HBox buttons = new HBox();
        {
            Button addPage = new Button("Ajouter une page");
            {
                addPage.setOnAction(
                        action -> displayPageEdition("add", "Votre histoire ici..."));
            }

            Button editPage = new Button("Modifier une page");
            {
                editPage.setOnAction(action ->
                        editionPresenter.clickOnEditPage(books.getSelectionModel().getSelectedIndex(),
                                pages.getSelectionModel().getSelectedIndex()));
            }

            Button insertPage = new Button("Insérer une page avant");
            {
                insertPage.setOnAction(action ->
                        editionPresenter.clickOnInsertPage(pages.getSelectionModel().getSelectedIndex()));
            }

            Button publish = new Button("Publier");
            {
                publish.setOnAction(action ->
                        editionPresenter.publishBook(books.getSelectionModel().getSelectedIndex()));
            }

            buttons.getChildren().add(addPage);
            buttons.getChildren().add(editPage);
            buttons.getChildren().add(insertPage);

            buttons.getChildren().add(publish);
        }

        pageMenu.getChildren().add(pages);
        pageMenu.getChildren().add(buttons);
    }

    private BorderPane mainPane = new BorderPane();

    private void setConfirm(String txt, EventHandler<ActionEvent> event){
        confirm.setText(txt);
        confirm.setOnAction(event);
    }

    @Override
    public void displayBookEdition(String isbn, String title, String summary){
        super.editIsbn.setText(isbn);
        super.editTitle.setText(title);
        super.editSummary.setText(summary);

        mainPane.setRight(edition);
        mainPane.setBottom(status);
    }

    @Override
    public void displayPageEdition(String mode, String txt) {

        super.texts.getChildren().clear();
        super.pageText.setText(txt);
        super.pageInfos.setVisible(true);

        switch (mode) {
            case "add":
                super.addDest.setVisible(false);
                super.remDest.setVisible(false);
                super.texts.getChildren().add(super.pageText);
                setConfirm("Ajouter", action ->
                        editionPresenter.addPage(super.pageText.getText(), books.getSelectionModel().getSelectedIndex()));
                break;

            case "edit":
                super.addDest.setVisible(true);
                super.remDest.setVisible(true);
                super.texts.getChildren().add(super.pageText);
                super.texts.getChildren().add(super.newDstText);
                super.newDstText.setText("Texte de la nouvelle destination...");
                setConfirm("Modifier", action -> editionPresenter.editPage(
                        books.getSelectionModel().getSelectedIndex(), pages.getSelectionModel().getSelectedIndex(),
                        pageText.getText(), newDstText.getText(), toPageAdd.getValue(), toPageRem.getValue()));
                break;

            case "insert":
                super.addDest.setVisible(false);
                super.remDest.setVisible(false);
                super.texts.getChildren().add(super.pageText);
                setConfirm("Insérer", action -> editionPresenter.insertPage(
                        books.getSelectionModel().getSelectedIndex(), pages.getSelectionModel().getSelectedIndex(),
                        super.pageText.getText()));
                break;
        }
    }

    @Override
    public void displayBookMenu(String item){
        observableBooks.add(item);
        mainPane.setLeft(bookMenu);
    }

    @Override
    public void displayPageMenu(String item) {
        super.status.setText("");
        observablePages.add(item);
        mainPane.setLeft(pageMenu);
    }

    @Override
    public void clearPageMenu(){
        observablePages.clear();
    }

    @Override
    public void setStatus(String msg, int type){
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

    private void delBook(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("(ㆆ _ ㆆ)");
        alert.setHeaderText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent()){}
        // Je sais pas à quoi ça sert mais ça doit être là j'en ai marre ça fait deux heures que
        // je cherche comment gérer les actions de cette alert et je comprends rien
        else if(result.get() == ButtonType.OK){
            editionPresenter.deleteBook(books.getSelectionModel().getSelectedIndex());
        }
    }

    @Override
    public void delPage(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("(ㆆ _ ㆆ)");
        alert.setHeaderText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent()){}
        // Je voulais utiliser une EventHandler pour éviter la répétition de code, mais je comprends toujours rien
        // tuez moi j'en ai marre
        else if(result.get() == ButtonType.OK){
            editionPresenter.deletePage(books.getSelectionModel().getSelectedIndex(),
                    pages.getSelectionModel().getSelectedIndex());
        }
    }


    @Override
    public void setPresenter(Presenter p) { this.editionPresenter = (EditionPresenter) p;}

    @Override
    public Parent getRoot() {
        return mainPane;
    }

}
