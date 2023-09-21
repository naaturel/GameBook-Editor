package org.helmo.gbeditor.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Shelf implements Iterable{

    List<Book> shelf;

    public Shelf(){ shelf = new ArrayList<>(); }

    public void refresh(List<Book> books){
        this.shelf.clear();
        addBooks(books);
    }

    public void addBooks(List<Book> b){
        shelf.addAll(b);
    }

    public Book getBookAt(int index){
        return shelf.get(index);
    }

    public boolean isEmpty(){
        return shelf.size() == 0;
    }

    @Override
    public Iterator iterator() { return shelf.iterator(); }
}
