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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import io.github.problem4j.core.Problem;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

abstract class AbstractProblemGsonTest {

  protected final String json =
      "{"
          + "\"type\":\"http://localhost/FATAL\","
          + "\"title\":\"problem\","
          + "\"status\":400,"
          + "\"detail\":\"A serious problem\","
          + "\"instance\":\"http://localhost/endpoint/12\","
          + "\"elements\":[\"A\",\"B\",\"C\"],"
          + "\"object\":{\"id\":200,\"name\":\"test name\"},"
          + "\"timestamp\":\"2018-10-01T10:43:21.221Z\","
          + "\"userid\":100"
          + "}";

  protected final Instant timestamp =
      LocalDateTime.of(2018, 10, 1, 10, 43, 21, 221000000).toInstant(ZoneOffset.UTC);

  // Extensions use Long for numeric values to match Gson's LONG_OR_DOUBLE deserialization strategy
  protected final Problem problem =
      Problem.builder()
          .type(URI.create("http://localhost/FATAL"))
          .title("problem")
          .status(400)
          .detail("A serious problem")
          .instance(URI.create("http://localhost/endpoint/12"))
          .extension("userid", 100L)
          .extension("timestamp", timestamp.toString())
          .extension("object", buildObject())
          .extension("elements", List.of("A", "B", "C"))
          .build();

  protected Object buildObject() {
    Map<String, Object> object = new LinkedHashMap<>();
    object.put("id", 200L);
    object.put("name", "test name");
    return object;
  }

  protected static Stream<Arguments> variousGsonConfigurations() {
    Gson factoryGson =
        new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .registerTypeAdapterFactory(new ProblemTypeAdapterFactory())
            .create();
    Gson directAdapterGson =
        new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .registerTypeHierarchyAdapter(
                Problem.class,
                new ProblemTypeAdapter(
                    new GsonBuilder()
                        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                        .create()))
            .create();
    return Stream.of(Arguments.of(factoryGson), Arguments.of(directAdapterGson));
  }
}
