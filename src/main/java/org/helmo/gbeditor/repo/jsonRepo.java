//package org.helmo.gbeditor.repo;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import org.helmo.gbeditor.exceptions.BookDataException;
//import org.helmo.gbeditor.exceptions.JsonFileException;
//import org.helmo.gbeditor.infrastructure.*;
//import org.helmo.gbeditor.models.Book;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// */
//public class jsonRepo {
//
//    private final Path filePath;
//    private final Path dirPath;
//    private List<Book> books = new ArrayList<>();
//
//    public jsonRepo(Path dpath, Path fpath){
//        this.dirPath = dpath;
//        this.filePath = fpath;
//        books = retrieveBooks();
//    }
//
//    /**
//     * Saves a book in a json file
//     * @param regNumb author's register number
//     * @param title book title
//     * @param summary book summary
//     * @return boolean    */
//    public boolean saveBook(String regNumb, String title, String summary, String fname, String lname)
//            throws IOException, BookDataException {
//
//        if (checkRegisterNumb(regNumb) && checkTitlelength(title) && checkSummarylength(summary)) {
//
//            createDir();
//            createFile();
//            Gson gson = new Gson();
//            List<Book> books = new ArrayList<>();
//
//            try {
//               books = retrieveBooks();
//            } catch (JsonFileException jse){
//
//            }
//
//            try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
//                    StandardOpenOption.TRUNCATE_EXISTING)) {
//
//                Book book = new Book(new Isbn(regNumb),
//                        title, summary, Mapper.toDTOEditor(author));
//                books.add(book);
//                gson.toJson(books, bw);
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * Retrieves all the books written in a json file
//     * @return A List containing the books. If this List is null, return an empty List
//     * @throws IOException
//     */
//    public List<Book> retrieveBooks() throws JsonFileException {
//
//        List<DTOBook> DTOBooks = new ArrayList<>();
//        Gson gson = new Gson();
//
//        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
//            DTOBooks = gson.fromJson(br, new TypeToken<List<DTOBook>>(){}.getType());
//        } catch (IOException ioe) {
//            throw new JsonFileException("");
//        }
//
//        return Mapper.toBooks(DTOBooks);
//
//    }
//
//    /**
//     * Creates a directory at a specific place if it doesn't exist yet
//     * @throws IOException
//     */
//    private void createDir() throws IOException {
//        if (!Files.exists(dirPath)) {
//            Files.createDirectories(dirPath);
//        }
//    }
//
//    private void createFile() throws IOException{
//        if(!Files.exists(filePath)){
//            Files.createFile(filePath);
//        }
//    }
//}

/*
try {
    jsonRepo repo = new jsonRepo(
            Paths.get(System.getProperty("user.home"), "ue36").toAbsolutePath(),
            Paths.get(System.getProperty("user.home"), "ue36", "q210044.json").toAbsolutePath());

    repo.retrieveBooks();
} catch (){

jsonRepo repo = new jsonRepo(
Paths.get(System.getProperty("user.home"), "ue36").toAbsolutePath(),
Paths.get(System.getProperty("user.home"), "ue36", "q210044.json").toAbsolutePath());
*/


