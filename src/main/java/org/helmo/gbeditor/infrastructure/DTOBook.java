package org.helmo.gbeditor.infrastructure;
import java.util.ArrayList;
import java.util.List;

/**
 * This class simply stores books data
 */
public class DTOBook {

    final String isbn;
    final String title;
    final String summary;
    final DTOEditor author;
    List<DTOPage> pages = new ArrayList<>();
    final boolean published;

    DTOBook(String isbn, String title, String summary, DTOEditor author, boolean published){
        this(isbn, title, summary, author, new ArrayList<>(), published);
    }

    DTOBook(String isbn, String title, String summary, DTOEditor author,  List<DTOPage> pages, boolean published){
        this.isbn = isbn;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.pages = pages;
        this.published = published;
    }
}
