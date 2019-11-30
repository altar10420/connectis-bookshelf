package com.example.bookshelf;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

public class BookshelfAppTest {

    private static final String BOOK_1 = "{\"title\":\"Java. Kompendium programisty\",\"author\":\"Herbert Schildt\","+
            "\"pagesSum\":1152,\"yearOfPublished\":2019,\"publishingHouse\":\"Helion\"}";

    private static final String BOOK_2 = "{\"title\":\"Python. Wprowadzenie.\",\"author\":\"Mark Lutz\","+
            "\"pagesSum\":1184,\"yearOfPublished\":2017,\"publishingHouse\":\"Helion\"}";

    private static final int APP_PORT = 8090;

    private BookshelfApp bookshelfApp;

    @BeforeAll
    public static void beforeAll() {
        RestAssured.port = APP_PORT;
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        bookshelfApp = new BookshelfApp(APP_PORT);
    }

    @AfterEach
    public void afterEach() {
        bookshelfApp.stop();
    }

    @Test
    public void addMethod_correctBody_shouldReturnStatus200() throws Exception {

        with().body(BOOK_1).when().post("/book/add").then().statusCode(200).body(startsWith("Book has been successfully added, id="));
    }

    @Test
    public void addMethod_fieldTypeMismatch_shouldReturnStatus500() {

        String bookWithFieldTypeMismatch = "{\"title\":\"Python. Wprowadzenie.\",\"author\":\"Mark Lutz\","+
                "\"pagesSum\":\"1184 pages\",\"yearOfPublished\":2017,\"publishingHouse\":\"Helion\"}";

        with().body(bookWithFieldTypeMismatch).when().post("book/add").then().statusCode(500);

//        with().body(BOOK_2).when().post("book/add").then().statusCode(500);
    }

    @Test
    public void addMethod_unexpectedField_shouldReturnStatus500() {
        with().body("{\"numberOfChapters\":10}").when().post("book/add").then().statusCode(500);
    }

}
