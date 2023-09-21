package org.helmo.gbeditor.views;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class EditionViewComponents {

    protected TextField editIsbn = new TextField();
    protected TextField editTitle = new TextField();
    protected TextArea editSummary = new TextArea();

    protected Spinner<Integer> toPageAdd = new Spinner<>(0, 999, 1);
    protected Spinner<Integer> toPageRem = new Spinner<>(0, 999, 1);

    protected TextArea pageText = new TextArea();{
        pageText.getStyleClass().add("textPage");
    }

    protected TextArea newDstText = new TextArea();{
        newDstText.getStyleClass().add("textPage");
    }

    protected VBox isbn = new VBox();{
        isbn.getChildren().add(new Label("ISBN-10"));
        isbn.getChildren().add(editIsbn);

        isbn.getStyleClass().add("editIsbn");
    }

    protected VBox title = new VBox();{
        title.getChildren().add(new Label("Titre"));
        title.getChildren().add(editTitle);

        title.getStyleClass().add("editTitle");
    }

    protected VBox summary = new VBox();{
        summary.getChildren().add(new Label("Résumé"));
        summary.getChildren().add(editSummary);

        summary.getStyleClass().add("editSummary");
    }

    protected VBox addDest = new VBox();{
        addDest.getChildren().add(new Label("Ajouter une destination\n(0 = optionnel)"));
        addDest.getChildren().add(toPageAdd);
    }

    protected VBox remDest = new VBox();{
        remDest.getChildren().add(new Label("Supprimer une destination\n (0 = optionnel)"));
        remDest.getChildren().add(toPageRem);
    }


    protected Label status = new Label();{
        status.getStyleClass().add("editionStatus");
    }

    HBox texts = new HBox();{
        texts.getStyleClass().add("pageTexts");
    }

    protected VBox pageInfos = new VBox();{

        HBox pathEdit = new HBox();{
            pathEdit.getChildren().add(addDest);
            pathEdit.getChildren().add(remDest);

            addDest.setVisible(false);
            remDest.setVisible(false);
            pathEdit.getStyleClass().add("numsPage");
        }

        pageInfos.getChildren().add(pathEdit);
        pageInfos.getChildren().add(texts);
        pageInfos.getStyleClass().add("pageInfos");
    }
    protected VBox bookInfos = new VBox();{

        bookInfos.getChildren().add(isbn);
        bookInfos.getChildren().add(title);
        bookInfos.getChildren().add(summary);
        bookInfos.getStyleClass().add("bookInfos");
    }

}
