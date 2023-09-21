package org.helmo.gbeditor.models;

import org.helmo.gbeditor.exceptions.BookDataException;

import java.util.regex.Pattern;

/**
 * This class simply stores author data
 */
public class Editor {
    private final String fname;
    private final String lname;

    public Editor(String tFname, String tLname){
        fname = tFname;
        lname = tLname;
    }

    public String getFname() {
        return fname;
    }

    public String getLname(){
        return lname;
    }

    /**
     * Check the register number validity
     * @param regNumb register number
     * @return true if the number is correct, otherwise throw a RegNumbException
     * @throws BookDataException
     */
    private boolean checkRegisterNumb(String regNumb) throws BookDataException {

        if(!Pattern.matches("[0-9]{6}", regNumb)){
            throw new BookDataException("Matricule incorrect.");
        }

        return true;
    }

}
