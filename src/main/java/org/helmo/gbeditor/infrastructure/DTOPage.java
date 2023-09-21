package org.helmo.gbeditor.infrastructure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DTOPage implements Comparable<DTOPage> {

    public int numSrc;
    public String texte;
    public List<DTOPath> dest;


    public DTOPage(int src, String txt){
        this(src, txt, new ArrayList<>());
    }

    public DTOPage(int src, String texte, List<DTOPath> dest){
        this.numSrc = src;
        this.texte = texte;
        this.dest = List.copyOf(dest);
    }

    @Override
    public int compareTo(DTOPage p) { return this.numSrc - p.numSrc; }
}
