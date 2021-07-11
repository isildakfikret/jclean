package com.codingpassenger.tools.jclean.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JResponse Unit Test")
class JResponseTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setup() {
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new JavaTimeModule());
    this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @DisplayName("# success()")
  @TestFactory
  Stream<DynamicTest> successTest() {
    record PersonResult(String status, String time, Person data) {
    }
    record BookResult(String status, String time, Book data) {
    }
    record Page(int page, int limit, int total, Collection<Book> content) {
    }
    record BooksResult(String status, LocalDateTime time, Page data) {
    }

    return Stream.of(
        DynamicTest
            .dynamicTest(
                "result should be correct, when the response created with person object and, null code, null message",
                () -> {
                  final var person = new Person("jane", "doe", 26);

                  final var response = JResponse.success(person);
                  final var responseJson = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
                  final var result = this.mapper.readValue(responseJson, PersonResult.class);

                  assertNotNull(result.time);
                  assertNotNull(result.status);
                  assertNotNull(result.data);
                  assertEquals(result.status, "SUCCESS");
                  assertEquals(result.data, person);
                }
            ),
        DynamicTest.dynamicTest(
            "result should be correct, when the response created with book object, null code, null message",
            () -> {
              final var book = new Book("Data Structure and Algorithms in Java", "Unknown");

              final var response = JResponse.success(book);
              final var json = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
              final var result = this.mapper.readValue(json, BookResult.class);

              assertNotNull(result.time);
              assertNotNull(result.status);
              assertNotNull(result.data);
              assertEquals(result.status, "SUCCESS");
              assertEquals(result.data, book);
            }
        ),
        DynamicTest.dynamicTest("result should be correct, when the response created for pagination", () -> {
          final var books = List.of(
              new Book("Refactoring", "Kent Beck & Martin Fowler"),
              new Book("Patterns of Enterprise Application Architecture", "Martin Fowler")
          );

          final var response = JResponse.success(1, 100, 1453, books);
          final var json = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
          final var result = this.mapper.readValue(json, BooksResult.class);

          assertNotNull(result.time);
          assertNotNull(result.status);
          assertNotNull(result.data);
          assertEquals(result.status, "SUCCESS");
          assertTrue(result.data.content.containsAll(books));
        })
    );
  }

  @DisplayName("# fail()")
  @TestFactory
  Stream<DynamicTest> failTest() {
    record BasicResponseResult(String status, String time, String data) {
    }
    record ValidationResponseResult(String status, LocalDateTime time, Collection<String> data) {
    }

    return Stream.of(
        DynamicTest.dynamicTest("result should be correct when the fail response created", () -> {
          final var message = "mock message";

          final var response = JResponse.fail(message);
          final var json = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
          final var result = this.mapper.readValue(json, BasicResponseResult.class);

          assertEquals(result.status, "FAIL");
          assertEquals(result.data, message);
        }),
        DynamicTest.dynamicTest("result should be correct when the fail response created with array", () -> {
          final var messages = List.of("username is required", "username must be between 4-16 characters");

          final var response = JResponse.fail(messages);
          final var json = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
          final var result = this.mapper.readValue(json, ValidationResponseResult.class);

          assertEquals(result.status, "FAIL");
          assertTrue(result.data.containsAll(messages));
        })
    );
  }

  @DisplayName("# error()")
  @TestFactory
  Stream<DynamicTest> errorTest() {
    record ErrorResponseResult(String status, String time, Integer code, String message) {
    }

    return Stream.of(
        DynamicTest.dynamicTest("result should be correct when the error response created", () -> {
          final var errCode = 100;
          final var errMessage = UUID.randomUUID().toString();

          final var response = JResponse.error(errCode, errMessage);
          final var json = this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
          final var result = this.mapper.readValue(json, ErrorResponseResult.class);

          assertEquals(result.status, "ERROR");
          assertEquals(result.code, errCode);
          assertEquals(result.message, errMessage);
        })
    );
  }

  private record Person(String firstName, String lastName, int age) {
  }

  private record Book(String title, String author) {
  }
}
