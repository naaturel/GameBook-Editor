package org.helmo.gbeditor.infrastructure;

import org.helmo.gbeditor.exceptions.DataLoadingException;
import org.helmo.gbeditor.exceptions.IsbnException;
import org.helmo.gbeditor.exceptions.PathException;
import org.helmo.gbeditor.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mapper {

    private static List<Path> toPath(List<DTOPath> dtoP) throws PathException{
        List<Path> path = new ArrayList<>();

        for (DTOPath p : dtoP) {
            path.add(new Path(p.text, p.destination));
        }
        return path;
    }

    public static List<Page> toPages(List<DTOPage> dtoPs) throws DataLoadingException{

        List<Page> pages = new ArrayList<>();
        Page page;
        try {
            for (DTOPage dtoPage : dtoPs) {
                page = new Page(dtoPage.numSrc, dtoPage.texte, toPath(dtoPage.dest));
                pages.add(page);
            }
        } catch (PathException pe){
            throw new DataLoadingException("Données invalides");
        }
        Collections.sort(pages);
        return pages;
    }

    public static Book toBook(DTOBook b) throws IsbnException {
        return new Book(new Isbn(b.isbn), b.title, b.summary, toEditor(b.author), b.published);
    }

    public static Editor toEditor(DTOEditor ed){
        return new Editor(ed.fname, ed.lname);
    }

    public static List<Book> toBooks(List<DTOBook> dtoB) throws IsbnException{

        List<Book> books = new ArrayList<>();

        for (DTOBook b : dtoB) {
            books.add(toBook(b));
        }

        return books;
    }

    public static DTOPath toDTOPath(Path p){ return new DTOPath(p.getDestination(), p.getText());}

    public static DTOPage toDTOPage(Page p){
        return new DTOPage(p.getNumSrc(), p.getSrcText());
    }

    public static DTOBook toDTOBook(Book b){
        return new DTOBook(b.getIsbn(), b.getTitle(), b.getSummary(),
                toDTOEditor(b.getAuthor()), toDTOPages(b.getPages()), b.isPublished());
    }

    public static DTOEditor toDTOEditor(Editor ed){
        return new DTOEditor(ed.getFname(), ed.getLname());
    }

    public static List<DTOPage> toDTOPages(List<Page> ps){
        List<DTOPage> dtoPs = new ArrayList<>();
        for (Page p : ps){
            dtoPs.add(new DTOPage(p.getNumSrc(), p.getSrcText(), toDTOPaths(p.getDestinations())));
        }
        return dtoPs;
    }

    private static List<DTOPath> toDTOPaths(List<Path> path){
        List<DTOPath> dtoPath = new ArrayList<>();
        for(Path p : path){
            dtoPath.add(toDTOPath(p));
        }

        return dtoPath;
    }

}
