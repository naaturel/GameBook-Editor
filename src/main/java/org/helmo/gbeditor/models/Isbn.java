package org.helmo.gbeditor.models;

import org.helmo.gbeditor.exceptions.IsbnException;

import java.util.regex.Pattern;

public class Isbn {

    private final String langPrefix = "2";
    private String regNumb;
    private String bookNumb;
    private String isbn;

    public Isbn(String regNumb, String bookNumb) throws IsbnException{
        this.regNumb = regNumb;
        checkRegNumb();
        this.bookNumb = bookNumb;
        this.isbn = langPrefix + regNumb + bookNumb + generateCtrlDigit(regNumb, bookNumb);
    }

    public Isbn(String isbn) throws IsbnException {
        this.isbn = isbn;
        checkIsbn();
    }

    private void checkIsbn() throws IsbnException {
        if (Pattern.matches("[0-9]{10}|[0-9]{9}x", isbn)) {

            String rNumb = isbn.substring(1, 7);
            String bNumb = isbn.substring(7, 9);
            String ctrl = isbn.substring(9, 10);
            String rightCtrl = generateCtrlDigit(rNumb, bNumb);

            if (!ctrl.equals(rightCtrl)) {
                throw new IsbnException("Chiffre de controle incorrect.\n" +
                        "Essayez " + langPrefix + rNumb + bNumb + rightCtrl);
            }

        } else {
            throw new IsbnException("Veuillez saisir un isbn à 10 chiffres.");
        }
    }

    private void checkRegNumb() throws IsbnException {
        if(!Pattern.matches("[0-9]{6}", regNumb)){
            throw new IsbnException("ISBN invalide. Veuillez saisir un matricule à 6 chiffres.");
        }
    }

    /**
     * Generate a control digit based on a lang prefix, a register number and a book number;
     * @return the isbn number created
     */
    private String generateCtrlDigit(String rNumb, String bNumb) {

        String ctrlString, isbn;

        int isbnCalc = 0;
        int ctrlDigit;

        int digit1 = Integer.parseInt(bNumb.substring(0,1));
        int digit2 = Integer.parseInt(bNumb.substring(1,2));

        isbn = langPrefix + rNumb + digit1 + digit2;

        for (int i = 0; i < isbn.length(); ++i) {
            isbnCalc += Integer.parseInt(isbn.substring(i, i + 1)) * (10 - i);
        }

        ctrlDigit = 11 - (isbnCalc % 11);

        ctrlString = ctrlDigit == 10 ? "x" :
                ctrlDigit == 11 ? "0" : Integer.toString(ctrlDigit);

        return ctrlString;

    }

    public String getIsbn(){ return this.isbn; }
}
