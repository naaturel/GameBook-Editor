package org.helmo.gbeditor.repo;

import org.helmo.gbeditor.exceptions.DataLoadingException;
import org.helmo.gbeditor.exceptions.IsbnException;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.models.Path;

import java.util.List;

public interface Repo {

    void savePage(Page p, String isbn);
    void editPage(Page p, String isbn, Path path, Integer remDest);
    void deletePage(Page p, String isbn, int nbPages);
    void insertPage(Book b, Page newPage, int from);

    List<Page> retrievePages(String isbn) throws DataLoadingException;
    void saveBook(Book b);
    void editBook(Book b, String oldIsbn);
    void deleteBook(String isbn);
    void publishBook(Book b);
    List<Book> retrieveBooks(String fname, String lname) throws IsbnException;

}
