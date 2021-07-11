# JClean

![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/isildakfikret/jclean/java-ci/master)

Contains two general models for API development:

    JResponse
    JException

## Examples

- **Successful Response**

```java
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.codingpassenger.tools.jclean.response;

class Demo {
  public static void main(String[] args) {
    final var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    per();

    final var person = new Person(UUID.randomUUID().toString(), "jane", "doe");
    final var response = JResponse.success(person);

    final var json = mapper.writerWithDefaultPrettyPrinter()
                           .writeValueAsString(response);
    System.out.println(json);

    final var result = mapper.readValue(json, PersonResponse.class);
    System.out.println(result);
  }
}

record Person(String id, String firstName, String lastName) {
}

record PersonResponse(String status, LocalDateTime time, Person data) {
}
```

Output:

```json
{
  "status": "SUCCESS",
  "time": "2021-07-11T12:28:27.369319791",
  "data": {
    "id": "c67fbf9c-a596-42cd-96e1-de116e5dfe8",
    "firstName": "jane",
    "lastName": "doe"
  }
}
```

```text
Person[id=c67fbf9c-a596-42cd-96e1-de116e5dfe8, firstName=jane, lastName=doe]
```

- **Failure Response**

```java
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.codingpassenger.tools.jclean.response;

class Demo {
  public static void main(String[] args) {
    final var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    final var response = JResponse.fail(List.of(
        "username is required",
        "username must be between 4-16 characters"
    ));
    final var json = mapper.writerWithDefaultPrettyPrinter()
                           .writeValueAsString(response);
    System.out.println(json);

    final var result = mapper.readValue(json, FailureResponse.class);
    System.out.println(result);
  }
}

record FailureResponse(String status, LocalDateTime time, Collection<String> data) {
}
```

Output:

```json
{
  "status": "FAIL",
  "time": "2021-07-11T12:33:32.110845714",
  "data": ["username is required", "username must be between 4-16 characters"]
}
```

```text
FailureResponse[status=FAIL, time=2021-07-11T12:35:15.962188017, data=[username is required, username must be between 4-16 characters]]
```

- **Error Response**

```java
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.codingpassenger.tools.jclean.response;

class Demo {
  public static void main(String[] args) {
    final var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    final var response = JResponse.error(1453, "make sure the game is synchronized");
    final var json = mapper.writerWithDefaultPrettyPrinter()
                           .writeValueAsString(response);
    System.out.println(json);

    final var result = mapper.readValue(json, ErrorResponse.class);
    System.out.println(result);
  }
}

record ErrorResponse(String status, LocalDateTime time, Integer code, String message) {
}
```

Output:

```json
{
  "status": "ERROR",
  "time": "2021-07-11T12:37:25.737365115",
  "code": 1453,
  "message": "make sure the game is synchronized"
}
```

```text
ErrorResponse[status=ERROR, time=2021-07-11T12:37:25.737365115, code=1453, message=make sure the game is synchronized]
```
