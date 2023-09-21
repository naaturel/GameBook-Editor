package org.helmo.gbeditor.presenters.ViewInterfaces;

public interface EditionViewInterface {

    void displayBookEdition(String isbn, String title, String summary);

    void displayPageEdition(String mode, String txt);

    void displayBookMenu(String item);

    void displayPageMenu(String item);

    void clearPageMenu();

    void delPage(String msg);

    void setStatus(String msg, int mode);
}
