package org.helmo.gbeditor.models;

import org.helmo.gbeditor.exceptions.PageException;

import java.util.LinkedList;
import java.util.List;

public class Page implements Comparable<Page> {

    private int numSrc;
    private String text;
    private List<Path> destinations;

    public Page(int numSrc, String text){
        this(numSrc, text, new LinkedList<>());
    }

    public Page(int src, String text, List<Path> dst){
        this.numSrc = src;
        this.text = text;
        this.destinations = List.copyOf(dst);
    }

    public void checkData() throws PageException {
        checkData(null);
    }

    public void checkData(Path path) throws PageException {
        checkText();
        if (path != null) {
            checkPathDuplication(path);
            checkCircularRef(path);
        }
    }

    private void checkText() throws PageException{
        if(text.isBlank()){
            throw new PageException("Le texte de la page source peut pas être vide");
        }
    }

    private void checkPathDuplication(Path path) throws PageException{
        for (Path p : destinations){
            if(p.equals(path)){
                throw new PageException("Cette destination existe déjà.");
            }
        }
    }

    private void checkCircularRef(Path path) throws PageException{
        if(path.getDestination() == this.numSrc){
            throw new PageException("La destination ne peut pas pointer vers la page sur la quelle elle se trouve");
        }
    }

    public int incSrcNum(){
        return this.numSrc += 1;
    }

    public String getNumSrcAsString() {
        return Integer.toString(numSrc);
    }
    public int getNumSrc(){ return this.numSrc;}
    public String getSrcText(){ return this.text; }
    public int getNbDestinations(){return this.destinations.size();}
    public List<Path> getDestinations(){ return this.destinations; }
    public String getDstText() {
        String txt = "";
        for (Path p : destinations){
            txt += p.getFormattedText();
        }

        return txt;
    }
    public void setNumSrc(int newSrc){ this.numSrc = newSrc; }
    public void setText(String text){ this.text = text; }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Page){
            if (((Page) o).getNumSrc() == this.numSrc) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Page p) { return this.numSrc - p.numSrc; }

}
