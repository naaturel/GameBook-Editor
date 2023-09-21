package org.helmo.gbeditor.models;

import org.helmo.gbeditor.exceptions.BookDataException;

import java.util.*;

/**
 * This class simply stores books data
 */
public class Book implements Comparable<Book> {

    private Isbn isbn;
    private String title;
    private String summary;
    private Editor author;
    private List<Page> pages;
    private boolean published;

    public Book(Isbn isbn, String title, String summary, Editor author) {
        this(isbn, title, summary, author, false);
    }
    public Book(Isbn isbn, String title, String summary, Editor author, boolean published) {
        this.isbn = isbn;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.pages = new ArrayList<>();
        this.published = published;
    }

    public String getIsbn(){ return this.isbn.getIsbn(); }
    public String getTitle(){ return this.title; }
    public String getSummary(){ return this.summary; }
    public String getAuthorFname(){ return author.getFname(); }
    public String getAuthorLname(){ return author.getLname(); }
    public Editor getAuthor(){ return this.author; }
    public List<Page> getPages(){return List.copyOf(pages);}
    public Page getPageAt(int index){ return pages.get(index); }
    public boolean isPublished(){return published;};

    public void clearPage(){ this.pages.clear(); }
    public int getNbPage(){ return this.pages.size(); }

    public int nbReferecendPagesFor(Page p){

        int count = 0;
        for(Page page : pages){
            for (Path path : page.getDestinations()){
                if(path.getDestination() == p.getNumSrc()){
                    count++;
                }
            }
        }

        return count;
    }

    public void swapPages(int index){
        for (int i = index; i < getNbPage(); ++i){
            this.pages.get(i).incSrcNum();
        }
    }

    public void refreshPages(){ pages.clear(); }

    public void addPages(List<Page> pages){
        this.pages.addAll(pages);
    }

    public void checkPublishPossibility() throws BookDataException{
        if(published){
            throw new BookDataException("Ce livre est déjà publié.");
        } else if(this.pages.isEmpty()){
            throw new BookDataException("Ce livre ne contient aucune page. Il ne peut pas être publié");
        }
    }

    public void checkData() throws  BookDataException{
        try{
            checkTitlelength(title);
            checkSummarylength(summary);
        } catch (BookDataException bde){
            throw new BookDataException(bde.getMessage());
        }
    }

    /**
     * Check the title length
     * @param title
     * @return true if the length is under 150. Otherwise throws a BookDataException
     * @throws BookDataException
     */
    private boolean checkTitlelength(String title) throws BookDataException{

        if(title.length() == 0){
            throw new BookDataException("Le titre ne peut pas être vide");
        } else if(title.length() >= 150){
            throw new BookDataException("Titre trop long.");
        }

        return true;
    }

    /**
     * Check the Summary length.
     * @param summary
     * @return true if the length is under 500. Otherwise throws a BookDataException
     * @throws BookDataException
     */
    private boolean checkSummarylength(String summary) throws BookDataException{

        if(summary.length() == 0){
            throw new BookDataException("Le résumé ne peut pas être vide");
        } else if(summary.length() >= 500){
            throw new BookDataException("Résumé trop long.");
        }

        return true;
    }

    public void checkUnexistingPointedPage(Path path) throws BookDataException {

        if (path.getDestination() != 0) {
            boolean existing = false;
            for (Page p : pages) {
                if (path.getDestination() == p.getNumSrc()) {
                    existing = true;
                }
            }
            if (!existing) {
                throw new BookDataException("La page de destination n'existe pas");
            }
        }
    }

    @Override
    public int compareTo(Book o) { return Integer.parseInt(this.isbn.getIsbn()) - Integer.parseInt(o.isbn.getIsbn());}
}
