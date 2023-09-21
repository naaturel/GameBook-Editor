package org.helmo.gbeditor.repo;

import org.helmo.gbeditor.exceptions.DataBaseException;
import org.helmo.gbeditor.exceptions.DataLoadingException;
import org.helmo.gbeditor.exceptions.IsbnException;
import org.helmo.gbeditor.exceptions.PathException;
import org.helmo.gbeditor.infrastructure.Mapper;
import org.helmo.gbeditor.infrastructure.GameBookStorage;
import org.helmo.gbeditor.infrastructure.GameBookStorageFactory;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Editor;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.models.Path;

import java.util.List;

public class dbRepo implements Repo {

    private String socket;
    public final String LOCAL_DB_URL = "jdbc:connection string here";

    private static GameBookStorageFactory factory;
    private GameBookStorage sqlr;

    public dbRepo(String socket) throws DataBaseException{

        this.factory = new GameBookStorageFactory(
                "com.mysql.cj.jdbc.Driver",
                LOCAL_DB_URL,
                "in21b10044",
                "0044");

        sqlr = factory.setConnection();
    }
    
    public void addAuthor(Editor e){
        sqlr.addAuthor(Mapper.toDTOEditor(e));
    }

    @Override
    public void savePage(Page p, String isbn) {
        sqlr.savePage(Mapper.toDTOPage(p), isbn);
    }

    @Override
    public void editPage(Page page, String isbn, Path path, Integer remDst) {
        sqlr.editPage(Mapper.toDTOPage(page), isbn, Mapper.toDTOPath(path), remDst);
    }

    @Override
    public void deletePage(Page p, String isbn, int nbPages) {
        sqlr.deletePage(Mapper.toDTOPage(p), isbn, nbPages);
    }

    @Override
    public void insertPage(Book b, Page newPage, int from) {
        sqlr.insertPages(Mapper.toDTOBook(b), Mapper.toDTOPage(newPage), from);
    }

    @Override
    public List<Page> retrievePages(String isbn) throws DataLoadingException {
        return Mapper.toPages(sqlr.retrievePages(isbn));
    }

    @Override
    public void deleteBook(String isbn) throws DataBaseException{
        sqlr.deleteBook(isbn);
    }

    @Override
    public void editBook(Book b, String oldIsbn) throws DataBaseException{
        sqlr.editBook(Mapper.toDTOBook(b), oldIsbn);
    }

    @Override
    public void publishBook(Book b){
        sqlr.publishBook(Mapper.toDTOBook(b));
    }

    @Override
    public void saveBook(Book b) { sqlr.saveBook(Mapper.toDTOBook(b)); }

    @Override
    public List<Book> retrieveBooks(String fname, String lname) throws DataBaseException, IsbnException {
        return Mapper.toBooks(sqlr.getBooks(fname, lname));
    }

}
