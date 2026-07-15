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
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.github.problem4j.core.Problem;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jspecify.annotations.Nullable;

final class ProblemTypeAdapter extends TypeAdapter<Problem> {

  private static final String TYPE = "type";
  private static final String TITLE = "title";
  private static final String STATUS = "status";
  private static final String DETAIL = "detail";
  private static final String INSTANCE = "instance";

  private static final Set<String> PROBLEM_MEMBERS =
      new HashSet<>(Arrays.asList(TYPE, TITLE, STATUS, DETAIL, INSTANCE));

  private final TypeAdapter<Object> objectAdapter;

  ProblemTypeAdapter(Gson gson) {
    this.objectAdapter = gson.getAdapter(TypeToken.get(Object.class));
  }

  @Override
  public void write(JsonWriter out, @Nullable Problem value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.beginObject();
    if (value.isTypeNonBlank()) {
      out.name(TYPE).value(value.getType().toString());
    }
    out.name(TITLE).value(value.getTitle());
    out.name(STATUS).value(value.getStatus());
    if (value.getDetail() != null) {
      out.name(DETAIL).value(value.getDetail());
    }
    if (value.getInstance() != null) {
      out.name(INSTANCE).value(value.getInstance().toString());
    }
    // Use a TreeMap to ensure alphabetical ordering of extension members in the output
    Map<String, Object> sortedExtensions = new TreeMap<>(value.getExtensions());
    for (Map.Entry<String, Object> entry : sortedExtensions.entrySet()) {
      if (!PROBLEM_MEMBERS.contains(entry.getKey())) {
        out.name(entry.getKey());
        objectAdapter.write(out, entry.getValue());
      }
    }
    out.endObject();
  }

  @Override
  public @Nullable Problem read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    in.beginObject();

    URI type = null;
    String title = null;
    int status = 0;
    String detail = null;
    URI instance = null;
    Map<String, Object> extensions = new TreeMap<>();

    while (in.hasNext()) {
      String name = in.nextName();
      switch (name) {
        case TYPE:
          type = readUri(in);
          break;
        case TITLE:
          title = readNullableString(in);
          break;
        case STATUS:
          status = readStatus(in);
          break;
        case DETAIL:
          detail = readNullableString(in);
          break;
        case INSTANCE:
          instance = readUri(in);
          break;
        default:
          Object extValue = objectAdapter.read(in);
          if (extValue != null) {
            extensions.put(name, extValue);
          }
          break;
      }
    }

    in.endObject();

    return Problem.builder()
        .type(type)
        .title(title)
        .status(status)
        .detail(detail)
        .instance(instance)
        .extensions(extensions)
        .build();
  }

  private @Nullable URI readUri(JsonReader in) throws IOException {
    if (in.peek() != JsonToken.STRING) {
      in.skipValue();
      return null;
    }
    String value = in.nextString();
    try {
      return URI.create(value);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  private @Nullable String readNullableString(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return in.nextString();
  }

  private int readStatus(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NUMBER) {
      return (int) in.nextDouble();
    }
    in.skipValue();
    return 0;
  }
}
