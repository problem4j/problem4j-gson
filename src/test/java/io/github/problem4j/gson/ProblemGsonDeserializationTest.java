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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProblemGsonDeserializationTest extends AbstractProblemGsonTest {

  @ParameterizedTest
  @MethodSource("variousGsonConfigurations")
  void givenVariousGsonConfigurations_whenDeserializing_shouldDeserializeProblem(Gson gson) {
    Problem deserializedProblem = gson.fromJson(json, Problem.class);

    assertEquals(problem.getType(), deserializedProblem.getType());
    assertEquals(problem.getTitle(), deserializedProblem.getTitle());
    assertEquals(problem.getStatus(), deserializedProblem.getStatus());
    assertEquals(problem.getDetail(), deserializedProblem.getDetail());
    assertEquals(problem.getInstance(), deserializedProblem.getInstance());

    assertEquals(problem.getExtensions().size(), deserializedProblem.getExtensions().size());

    for (String key : problem.getExtensions().keySet()) {
      assertTrue(deserializedProblem.getExtensions().containsKey(key));
      assertEquals(problem.getExtensions().get(key), deserializedProblem.getExtensions().get(key));
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"http://exa mple.com", "http://example.com/<>", "http://[::1"})
  @NullSource
  void givenTypeInvalidUri_whenDeserializing_shouldDeserializeWithBlankType(String type) {
    Gson gson =
        new GsonBuilder().registerTypeAdapterFactory(new ProblemTypeAdapterFactory()).create();

    String json =
        """
        {
          "type"     : %s,
          "title"    : "Hello World",
          "status"   : 99,
          "instance" : %s
        }
        """
            .formatted(quoteIfNotNull(type), quoteIfNotNull(type));

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(Problem.BLANK_TYPE, problem.getType());
    assertEquals("Hello World", problem.getTitle());
    assertEquals(99, problem.getStatus());
    assertNull(problem.getInstance());
  }

  @ParameterizedTest
  @ValueSource(strings = {"\"string\"", "false", "true"})
  @NullSource
  void givenInvalidStatus_whenDeserializing_shouldDeserializeToZero(String status) {
    Gson gson =
        new GsonBuilder().registerTypeAdapterFactory(new ProblemTypeAdapterFactory()).create();

    String json =
        """
        {
          "type"     : "http://example.com/type",
          "title"    : "Hello World",
          "status"   : %s,
          "instance" : "http://example.com/instance"
        }
        """
            .formatted(status);

    Problem problem = gson.fromJson(json, Problem.class);

    assertEquals(0, problem.getStatus());
  }

  private String quoteIfNotNull(String value) {
    return value != null ? "\"" + value + "\"" : "null";
  }
}
