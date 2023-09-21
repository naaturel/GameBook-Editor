package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.exceptions.*;
import org.helmo.gbeditor.models.*;
import org.helmo.gbeditor.repo.Repo;
import org.helmo.gbeditor.views.EditionView;
import org.helmo.gbeditor.presenters.ViewInterfaces.EditionViewInterface;
import org.helmo.gbeditor.views.Views;

import java.util.Iterator;
import java.util.List;

public class EditionPresenter implements Presenter {

    private MainPresenter mainPresenter;
    private EditionViewInterface view;
    private Repo repository;
    private Shelf shelf;

    public EditionPresenter(EditionView v, MainPresenter p, Repo repo) {
        this.mainPresenter = p;
        this.view = v;
        this.repository = repo;
        this.shelf = new Shelf();

        refreshBookList();
        addBooksToView();
    }

    /**
     * Adds books in the side book menu and display it
     */
    private void addBooksToView() {
        if (!shelf.isEmpty()) {
            Iterator<Book> itShelf = shelf.iterator();
            while(itShelf.hasNext()) {
                Book b = itShelf.next();
                view.displayBookMenu(String.format("%-25s %-25s %15s %s\n%s",
                        b.getIsbn(), b.getTitle(), b.getAuthorFname(), b.getAuthorLname(), b.isPublished() ? "[publié]":"[non publié]"));
            }
        } else {
            view.setStatus("Aucun livre enregistré.", 0);
        }
    }

    private void refreshPagesList(int bookIndex){
        view.clearPageMenu();
        shelf.getBookAt(bookIndex).clearPage();
        addPagesToView(bookIndex);
    }

    /**
     * Reloads all books informations in the database
     */
    private void refreshBookList() {
        try {
            shelf.refresh(repository.retrieveBooks(mainPresenter.getFname(), mainPresenter.getLname()));
        } catch (IsbnException ie) {
            view.setStatus(ie.getMessage(), 0);
        }
    }

    private void addPagesToView(int bookIndex){
        Book b = shelf.getBookAt(bookIndex);

        try {
            List<Page> pages = repository.retrievePages(b.getIsbn());

            if (!pages.isEmpty()) {
                b.addPages(pages);
                for (Page p : pages) {
                    view.displayPageMenu(String.format("#%-10s \n%s \n%s ",
                            p.getNumSrcAsString(), p.getSrcText(), p.getDstText()));
                }
            } else {
                view.displayPageMenu("Ce livre ne contient aucune page");
            }
        } catch (DataLoadingException dle){
            view.setStatus(dle.getMessage(), 0);
        }
    }

    /**
     * Event called when the user press the edit page button. Displays an edition menu filled with
     * editable page informations
     *
     * @param bookIndex index of the book that contains the page. Allows to retrieve the entire book informations from
     *                  the book list contained in this class
     * @param pageIndex index of the page in the. Allow the retrieve the entire page informations from the page
     *                  list contained in the book previously retrieved
     */
    public void clickOnEditPage(int bookIndex, int pageIndex) {
        if (pageIndex != -1) {
            Page p = shelf.getBookAt(bookIndex).getPageAt(pageIndex);
            view.displayPageEdition("edit", p.getSrcText());
        } else {
            view.setStatus("Veuillez séléctionner une page.", 0);
        }
    }

    public void clickOnInsertPage(int pageIndex) {
        if (pageIndex != -1) {
            view.displayPageEdition("insert", "Votre histoire ici...");
        } else {
            view.setStatus("Veuillez séléctionner une page", 0);
        }
    }

    public void clickOnDeletePage(int bookIndex, int pageIndex) {

        Book b = shelf.getBookAt(bookIndex);
        Page p = b.getPageAt(pageIndex);
        int nbPath = b.nbReferecendPagesFor(p);
        if (nbPath <= 1) {
            deletePage(bookIndex, pageIndex);
        } else {
            view.delPage("Cette page est référencée dans " + nbPath + " autre(s) page(s), êtes-vous sûr ?");
        }
    }

    public void addPage(String srcTxt, int bookIndex){
        try {
            Book b = shelf.getBookAt(bookIndex);
            Page p = new Page(b.getNbPage() + 1, srcTxt);
            p.checkData();
            repository.savePage(p, b.getIsbn());
            b.refreshPages();
            refreshPagesList(bookIndex);
            view.setStatus("Page ajoutée", 1);
        } catch (DataBaseException dbe){
            view.setStatus(dbe.getMessage(), 0);
        } catch(PageException pe){
            view.setStatus(pe.getMessage(), 0);
        }
    }

    public void editPage(int bookIndex, int pageIndex, String pageText, String newDstText, Integer newDst, Integer remDst) {

        if (pageIndex != -1) {
            try {
                Book b = shelf.getBookAt(bookIndex);
                Page p = b.getPageAt(pageIndex);
                p.setText(pageText);
                Path newPath = new Path(newDstText, newDst);
                b.checkUnexistingPointedPage(newPath);
                p.checkData(newPath);

                repository.editPage(p, b.getIsbn(), newPath, remDst);
                b.refreshPages();
                refreshPagesList(bookIndex);
                view.setStatus("Page modifiée", 1);
            } catch (DataBaseException | BookDataException | PathException | PageException e) {
                view.setStatus(e.getMessage(), 0);
            }
        } else {
            view.setStatus("Veuillez sélectionner une page", 0);
        }
    }

    public void insertPage(int bookIndex, int pageIndex, String srcTxt) {
        try {
            int insertedPageNum = pageIndex + 1;
            int startChangesAt = pageIndex;
            Page p = new Page(insertedPageNum, srcTxt);

            Book newBook = shelf.getBookAt(bookIndex);
            newBook.swapPages(pageIndex);

            repository.insertPage(newBook, p, startChangesAt);
            refreshBookList();
            newBook.refreshPages();
            refreshPagesList(bookIndex);
            view.setStatus("Page insérée", 1);
        } catch (DataBaseException dbe){
            view.setStatus(dbe.getMessage(), 0);
        } /*catch(PageException pe){
            view.setStatus(pe.getMessage());
        }*/
    }

    //TODO : Fix user bug

    /**
     * Event called when the user choose a book in the side book menu
     *
     * @param index index of the book choose in the side menu. Allows to retrieve the entire book informations from
     *              the book list contained in this class
     */
    public void clickOnBook(int index) {

        if (index != -1) {
            try {
                Book b = shelf.getBookAt(index);
                view.displayBookEdition(b.getIsbn(), b.getTitle(), b.getSummary());
                //b.addPages(repository.retrievePages(b.getIsbn()));
                addPagesToView(index);
            } catch (DataBaseException dbe) {
                view.setStatus(dbe.getMessage(), 0);
            }
        } else {
            view.setStatus("Veuillez séléctionner un livre.", 0);
        }
    }

    /**
     * Event called when the user has edited a book informations and click on edit button. Displays an edition menu filled
     * with editable book informations
     *
     * @param oldBookIndex Index of the previous book state to make it correspond with the edited book in the case of
     *                     the isbn has changed because the user edited it.
     * @param isbn         edited isbn
     * @param title        edited title
     * @param summary      edited summary
     */
    public void editBook(int oldBookIndex, String isbn, String title, String summary) {

        String oldIsbn = shelf.getBookAt(oldBookIndex).getIsbn();

        Book book = shelf.getBookAt(oldBookIndex);

        if(!book.isPublished()){
            try {
                Book newBook = new Book(new Isbn(isbn), title, summary,
                        new Editor(mainPresenter.getFname(), mainPresenter.getLname()));

                repository.editBook(newBook, oldIsbn);
                mainPresenter.goTo(Views.EDITION);
                view.setStatus("Livre modifié.", 1);
            } catch (DataBaseException dbe) {
                view.setStatus(dbe.getMessage(), 0);
            } catch (IsbnException ie) {
                view.setStatus(ie.getMessage(), 0);
            }
        } else {
            view.setStatus("Ce livre est publié. Impossible de de le modifier", 0);
        }
    }


    public void publishBook(int bookIndex){
        if(bookIndex != -1){
            try{
                shelf.getBookAt(bookIndex).checkPublishPossibility();
                repository.publishBook(shelf.getBookAt(bookIndex));
                view.setStatus("Livre publié !", 1);
                mainPresenter.goTo(Views.EDITION);

            } catch (DataBaseException | BookDataException e) {
                view.setStatus(e.getMessage(), 0);
            }
        } else {
            view.setStatus("Veuillez séléctionner un livre", 0);
        }
    }

    /**
     * Allows the delete a book from the database
     *
     * @param bookIndex index of the book that contains the page. Allows to retrieve the entire book informations from
     *                  the book list contained in this class
     */
    public void deleteBook(int bookIndex) {
        try {
            repository.deleteBook(shelf.getBookAt(bookIndex).getIsbn());
            mainPresenter.goTo(Views.EDITION);
            view.setStatus("Livre supprimé.", 1);
        } catch (DataBaseException dbe) {
            view.setStatus(dbe.getMessage(), 0);
        }
    }

    public void deletePage(int bookIndex, int pageIndex){
        try{
            Book b = shelf.getBookAt(bookIndex);
            String isbn = b.getIsbn();
            Page p = b.getPageAt(pageIndex);
            repository.deletePage(p, isbn, b.getNbPage());
            refreshBookList();
            refreshPagesList(bookIndex);
            view.setStatus("Page supprimée", 1);
        } catch(DataBaseException dbe){
            view.setStatus(dbe.getMessage(), 0);
        }
    }

    @Override
    public void goTo(Views name) {
        mainPresenter.goTo(name);
    }

}