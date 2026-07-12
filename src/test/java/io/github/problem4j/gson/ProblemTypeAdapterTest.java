/*
 * Copyright 2025-2026 The Problem4J Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.problem4j.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.problem4j.core.Problem;
import org.junit.jupiter.api.Test;

class ProblemTypeAdapterTest {

  private final Gson gson =
      new GsonBuilder().registerTypeAdapterFactory(new ProblemTypeAdapterFactory()).create();

  @Test
  void givenTypeAsNumber_whenDeserializing_thenTypeShouldBeBlank() {
    String json =
        """
        {"type":123,"title":"test","status":400}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(Problem.BLANK_TYPE, problem.getType());
    assertEquals("test", problem.getTitle());
    assertEquals(400, problem.getStatus());
  }

  @Test
  void givenTypeAsBoolean_whenDeserializing_thenTypeShouldBeBlank() {
    String json =
        """
        {"type":true,"title":"test","status":400}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(Problem.BLANK_TYPE, problem.getType());
  }

  @Test
  void givenTypeAsObject_whenDeserializing_thenTypeShouldBeBlank() {
    String json =
        """
        {"type":{"nested":"value"},"title":"test","status":400}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(Problem.BLANK_TYPE, problem.getType());
  }

  @Test
  void givenInstanceAsNumber_whenDeserializing_thenInstanceShouldBeNull() {
    String json =
        """
        {"title":"test","status":400,"instance":123}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertNull(problem.getInstance());
  }

  @Test
  void givenInstanceAsBoolean_whenDeserializing_thenInstanceShouldBeNull() {
    String json =
        """
        {"title":"test","status":400,"instance":true}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertNull(problem.getInstance());
  }

  @Test
  void givenInstanceAsArray_whenDeserializing_thenInstanceShouldBeNull() {
    String json =
        """
        {"title":"test","status":400,"instance":[1,2,3]}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertNull(problem.getInstance());
  }

  @Test
  void givenStatusAsNull_whenDeserializing_thenStatusShouldBeZero() {
    String json =
        """
        {"title":"test","status":null}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(0, problem.getStatus());
  }

  @Test
  void givenStatusAsBoolean_whenDeserializing_thenStatusShouldBeZero() {
    String json =
        """
        {"title":"test","status":true}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(0, problem.getStatus());
  }

  @Test
  void givenStatusAsString_whenDeserializing_thenStatusShouldBeZero() {
    String json =
        """
        {"title":"test","status":"bad"}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(0, problem.getStatus());
  }

  @Test
  void givenReservedNameAsExtension_whenSerializing_thenExtensionShouldBeIgnored() {
    Problem problem =
        Problem.builder()
            .title("Hello World")
            .status(99)
            .extension("type", "someValue")
            .extension("title", "someValue")
            .extension("status", "someValue")
            .extension("detail", "someValue")
            .extension("instance", "someValue")
            .build();

    String json = gson.toJson(problem, Problem.class);

    assertEquals("{\"title\":\"Hello World\",\"status\":99}", json);
  }

  @Test
  void givenExtensionWithNullValue_whenSerializing_thenExtensionShouldBeIgnored() {
    Problem problem =
        Problem.builder()
            .title("Hello World")
            .status(99)
            .extension(Problem.extension("custom", null))
            .build();

    String json = gson.toJson(problem, Problem.class);

    assertEquals("{\"title\":\"Hello World\",\"status\":99}", json);
  }

  @Test
  void givenTitleAsNull_whenDeserializing_thenTitleShouldFallBackToStatusReasonPhrase() {
    String json =
        """
        {"title":null,"status":400}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals("Bad Request", problem.getTitle());
  }

  @Test
  void givenDetailAsNull_whenDeserializing_thenDetailShouldBeNull() {
    String json =
        """
        {"title":"test","status":400,"detail":null}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertNull(problem.getDetail());
  }

  @Test
  void givenNullProblem_whenSerializing_thenJsonShouldBeNull() {
    String json = gson.toJson(null, Problem.class);

    assertEquals("null", json);
  }

  @Test
  void givenJsonNull_whenDeserializing_thenResultShouldBeNull() {
    Problem problem = gson.fromJson("null", Problem.class);

    assertNull(problem);
  }

  @Test
  void givenExtensionWithNullNameInJson_whenDeserializing_thenExtensionShouldBeIgnored() {
    String json =
        """
        {"title":"test","status":400,"custom":null}
        """;

    Problem problem = gson.fromJson(json, Problem.class);

    assertTrue(problem.getExtensions().isEmpty());
  }
}
