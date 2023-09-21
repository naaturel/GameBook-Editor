package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.exceptions.BookDataException;
import org.helmo.gbeditor.exceptions.DataBaseException;
import org.helmo.gbeditor.exceptions.IsbnException;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Editor;
import org.helmo.gbeditor.models.Isbn;
import org.helmo.gbeditor.presenters.ViewInterfaces.CreationViewInterface;
import org.helmo.gbeditor.repo.Repo;
import org.helmo.gbeditor.views.CreationView;
import org.helmo.gbeditor.views.Views;
import java.util.List;

public class CreationPresenter implements Presenter {

    private CreationViewInterface view;
    private MainPresenter mainPresenter;
    private List<Book> books;
    private Repo repository;

    public CreationPresenter(CreationView view, MainPresenter mp, Repo repo){
        this.view = view;
        this.repository = repo;
        this.mainPresenter = mp;
    }

    /**
     * Called when the save button is pressed. It saves the book written in the bookEditor view by retrieving
     * its information. If the book is successfully saved the message "Modifications enregistrées" is shown
     * If an error occurs, it's caught and a corresponding message is displayed to warn the user
     */
    public void clickOnSave(String regNumb, String title, String summary, String fname, String lname) {

        try {
            books = repository.retrieveBooks(getNames(1), getNames(2));
            String bookNumb = books.size()+1 < 10 ?
                    String.format("0%d", books.size()+1) : Integer.toString(books.size()+1);

            Isbn isbn = new Isbn(regNumb, bookNumb);
            Book book = new Book(isbn, title, summary, new Editor(fname, lname));
            book.checkData();

            repository.saveBook(book);
            view.setStatus("Livre ajouté", 1);
        } catch (BookDataException bde) {
            view.setStatus(bde.getMessage(), 0);
        } catch (DataBaseException dbe){
            view.setStatus(dbe.getMessage(), 0);
        } catch (IsbnException ie) {
            view.setStatus(ie.getMessage(), 0);
        }
    }

    public String getNames(int index){
        return index == 1 ? mainPresenter.getFname() : mainPresenter.getLname();
    }

    @Override
    public void goTo(Views name) {
        mainPresenter.goTo(name);
    }
}
