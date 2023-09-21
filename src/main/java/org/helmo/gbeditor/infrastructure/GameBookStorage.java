package org.helmo.gbeditor.infrastructure;

import org.helmo.gbeditor.exceptions.DataBaseException;

import java.sql.*;
import java.util.*;

public class GameBookStorage implements AutoCloseable {

    private final Connection connection;

    public GameBookStorage(Connection con){
        this.connection = con;
    }

    //=====================================SELECTION=====================================

    private final String GET_BOOKS =
            "SELECT b.isbn, b.title, b.summary, a.firstname, a.lastname, b.published FROM books b " +
            "JOIN author a ON a.aId = b.aId WHERE a.firstname = ? AND a.lastname = ? ";

    private final String GET_AUTHOR = "SELECT firstname, lastname FROM author WHERE firstname = ? AND lastname = ?";

    private final String GET_PAGES = "SELECT pageNum, texte FROM page WHERE isbn = ?";

    private final String GET_DESTINATION_PAGES =
            "SELECT (SELECT p.pageNum FROM page p JOIN path pa ON pa.pIdTo = p.pId WHERE pat.pathId = pa.pathId), " +
            "pat.texte " +
            "FROM page pag " +
            "JOIN path pat ON pat.pIdFrom = pag.pId " +
            "WHERE pag.isbn = ? AND pag.pageNum = ?";

    //=====================================ADDITION=====================================

    private final String ADD_BOOK = "INSERT INTO books VALUES (?,?,?,?)";

    private final String ADD_AUTHOR = "INSERT INTO author VALUES (null, ?, ?)";

    private final String ADD_PAGE = "INSERT INTO page VALUES(null, ?, ?, ?)";

    private final String ADD_PATH =
            "INSERT INTO path VALUES(null, (SELECT pId FROM page WHERE isbn = ? AND pageNum = ?), " +
            "(SELECT pId FROM page WHERE isbn = ? AND pageNum = ?), ?)";

    //=====================================EDITION=====================================

    private final String EDIT_BOOK = "UPDATE books SET isbn = ?, title = ?, summary = ? WHERE isbn = ?";

    private final String EDIT_PAGE = "UPDATE page SET texte = ? WHERE isbn = ? AND pageNum = ?";

    private final String EDIT_NUM_PAGE = "UPDATE page SET pageNum = ? WHERE isbn = ? AND pageNum = ?";

    private final String PUBLISH_BOOK = "UPDATE books SET published = ? WHERE isbn = ? ";

    //=====================================DELETION=====================================

    private final String DELETE_BOOK = "DELETE FROM books WHERE isbn = ?";

    private final String DELETE_PATH =
            "DELETE FROM path WHERE pIdFrom = (SELECT pId FROM page WHERE isbn = ? AND pageNum = ?) " +
            "AND pIdTo = (SELECT pId FROM page WHERE isbn = ? AND pageNum = ?)";

    private final String DELETE_PAGE = "DELETE FROM page WHERE isbn = ? AND pageNum = ?";

    //=====================================AUTHOR=============================================

    private boolean existingAuthor(DTOEditor ed) throws DataBaseException{
        try (PreparedStatement statement =
                     connection.prepareStatement(GET_AUTHOR)) {

            statement.setString(1, ed.fname);
            statement.setString(2, ed.lname);


            try(ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    return true;
                }
                return false;
            }
        } catch (SQLException sqle) {
            throw new DataBaseException("DataBase exception : " + sqle.getMessage(), sqle.getCause());
        }
    }

    public void addAuthor(DTOEditor ed) throws DataBaseException{
        if(!existingAuthor(ed)) {
            try (PreparedStatement statement =
            connection.prepareStatement(ADD_AUTHOR)) {

                statement.setString(1, ed.fname);
                statement.setString(2, ed.lname);
                statement.executeUpdate();

            } catch (SQLException sqle) {
                throw new DataBaseException("DataBase exception : " + sqle.getMessage(), sqle.getCause());
            }
        }
    }


    //=====================================BOOK============================================

    public List<DTOBook> getBooks(String firstname, String lastname) throws DataBaseException{

        List<DTOBook> books = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(GET_BOOKS)) {

            statement.setString(1, firstname);
            statement.setString(2, lastname);

            try(ResultSet resSet = statement.executeQuery()) {

                while (resSet.next()) {
                    //p = getPages(resSet.getString(1));
                    DTOBook dtoBook =
                    new DTOBook(resSet.getString(1), resSet.getString(2), resSet.getString(3),
                            new DTOEditor(resSet.getString(4), resSet.getString(5)), resSet.getBoolean(6));

                    books.add(dtoBook);
                }
            }

            return books;
        } catch (SQLException sqle) {
            throw new DataBaseException("Impossible de récupérer les livres en base de données", sqle.getCause());
        }
    }

    public void saveBook(DTOBook b) throws DataBaseException{
        try (PreparedStatement statement =
                     connection.prepareStatement(ADD_BOOK)) {

            statement.setString(1, b.isbn);
            statement.setString(2, b.title);
            statement.setString(3, b.summary);
            statement.setInt(4,1);
            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException("DataBase exception : " + sqle.getMessage(), sqle.getCause());
        }
    }

    public void editBook(DTOBook b, String oldIsbn) throws DataBaseException{
        try (PreparedStatement statement =
                     connection.prepareStatement(EDIT_BOOK)) {

            statement.setString(1, b.isbn);
            statement.setString(2, b.title);
            statement.setString(3, b.summary);
            statement.setString(4, oldIsbn);
            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException("DataBase exception : " + sqle.getMessage(), sqle.getCause());
        }
    }

    public void publishBook(DTOBook dtoB){
        try(PreparedStatement statement = connection.prepareStatement(PUBLISH_BOOK)) {

            statement.setInt(1, 1);
            statement.setString(2, dtoB.isbn);
            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException("DataBase exception : " + sqle.getMessage(), sqle.getCause());
        }
    }

    public void deleteBook(String isbn) throws DataBaseException{
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BOOK)) {

            statement.setString(1, isbn);
            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException("DataBase exception : " + sqle.getMessage(), sqle.getCause());
        }
    }

    //=====================================PAGE============================================

    private List<DTOPath> getDestinations(String isbn, int numPage){

        LinkedList<DTOPath> dst = new LinkedList<>();

        try (PreparedStatement statement = connection.prepareStatement(GET_DESTINATION_PAGES)) {

            statement.setString(1, isbn);
            statement.setInt(2, numPage);

            try(ResultSet resSet = statement.executeQuery()) {

                while (resSet.next()) {
                    dst.add(new DTOPath(resSet.getInt(1),  resSet.getString(2)));
                }
            }

            return dst;

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    public List<DTOPage> retrievePages(String isbn){
        List<DTOPage> pages = new ArrayList<>();
        DTOPage p;
        try (PreparedStatement statement = connection.prepareStatement(GET_PAGES)) {

            statement.setString(1, isbn);

            try(ResultSet resSet = statement.executeQuery()) {
                int num;
                String txt;
                while (resSet.next()) {
                    num = resSet.getInt(1);
                    txt = resSet.getString(2);
                    p = new DTOPage(num, txt, getDestinations(isbn, num));
                    pages.add(p);
                }
            }

            return pages;
        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    //TODO : Refresh pages with generated keys
    public void editPage(DTOPage p, String isbn, DTOPath path, Integer remDst){

        editPageText(p, isbn);

        if(path.destination != 0) {
            addPath(p.numSrc, isbn, path.destination, path.text);
        }
        if(!remDst.equals("")) {
            remPath(p.numSrc, isbn, remDst);
        }
    }


    public void savePage(DTOPage p, String isbn){

        try (PreparedStatement statement = connection.prepareStatement(ADD_PAGE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, p.numSrc);
            statement.setString(2, p.texte);
            statement.setString(3, isbn);

            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    public void deletePage(DTOPage p, String isbn, int maxSrc){

        try (PreparedStatement statement = connection.prepareStatement(EDIT_NUM_PAGE, Statement.RETURN_GENERATED_KEYS)) {

            try(PreparedStatement statement2 = connection.prepareStatement(DELETE_PAGE)){
                statement2.setString(1, isbn);
                statement2.setInt(2, p.numSrc);
                statement2.executeUpdate();
            }

            for(int i = p.numSrc; i < maxSrc ; i++){
                statement.setInt(1, i);
                statement.setString(2, isbn);
                statement.setInt(3, i+1);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    public void insertPages(DTOBook b, DTOPage newPage, int from) {

        try (PreparedStatement statements = connection.prepareStatement(EDIT_NUM_PAGE)) {

            int decrease;
            for (int i = b.pages.size() - 1; i >= 0; i--) {
                decrease = i >= from ? 1 : 0;

                statements.setInt(1, b.pages.get(i).numSrc);
                statements.setString(2, b.isbn);
                statements.setInt(3, b.pages.get(i).numSrc - decrease);
                statements.addBatch();

            }
            statements.executeBatch();

            savePage(newPage, b.isbn);

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    private void editPageText(DTOPage p, String isbn){
        try (PreparedStatement statement = connection.prepareStatement(EDIT_PAGE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, p.texte);
            statement.setString(2, isbn);
            statement.setInt(3, p.numSrc);

            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    private void addPath(int src, String isbn, int dest,String txt){
        try (PreparedStatement statement = connection.prepareStatement(ADD_PATH, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, isbn);
            statement.setInt(2, src);
            statement.setString(3, isbn);
            statement.setInt(4, dest);
            statement.setString(5, txt);

            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    private void remPath(int src, String isbn, Integer remDst){
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PATH, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, isbn);
            statement.setInt(2, src);
            statement.setString(3, isbn);
            statement.setInt(4, remDst);

            statement.executeUpdate();

        } catch (SQLException sqle) {
            throw new DataBaseException(sqle.getMessage(), sqle.getCause());
        }
    }

    @Override
    public void close() throws DataBaseException {
        try {
            connection.close();
        } catch (SQLException sqle){
            throw new DataBaseException("Déconnexion impossible", sqle.getCause());
        }
    }
}