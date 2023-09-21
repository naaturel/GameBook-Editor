package org.helmo.gbeditor.models;

import org.helmo.gbeditor.exceptions.PathException;

public class Path {

    private String text;
    private int destination;

    public Path(String txt, int dst) throws PathException {
        this.text = txt;
        this.destination = dst;
        checkData();
    }

    public String getFormattedText(){
        return this.text + " (Allez à la page " + destination + ")\n";
    }

    public String getText(){ return this.text;}

    public int getDestination() { return this.destination; }

    private void checkData() throws PathException {
        checkTextLength();
    }


    private void checkTextLength() throws PathException{
        if(destination != 0 && text.isBlank()){
            throw new PathException("Le texte ne peut pas être vide si la destination est remplie");
        }
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Path){
            if (((Path) o).getDestination() == this.getDestination()) {
                return true;
            }
        }
        return false;
    }
}

