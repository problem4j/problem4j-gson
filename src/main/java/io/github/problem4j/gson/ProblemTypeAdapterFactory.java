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
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import io.github.problem4j.core.Problem;
import org.jspecify.annotations.Nullable;

/**
 * Gson {@link TypeAdapterFactory} for {@link Problem}. Register this factory with your {@code
 * GsonBuilder} to enable serialization and deserialization of {@link Problem} instances.
 *
 * <p>Usage example:
 *
 * <pre>{@code
 * Gson gson = new GsonBuilder()
 *     .registerTypeAdapterFactory(new ProblemTypeAdapterFactory())
 *     .create();
 * }</pre>
 *
 * @see com.google.gson.GsonBuilder#registerTypeAdapterFactory(TypeAdapterFactory)
 * @since 1.0.0
 */
public final class ProblemTypeAdapterFactory implements TypeAdapterFactory {

  /**
   * Creates a {@link TypeAdapter} for the given type if it is assignable from {@link Problem},
   * otherwise returns {@code null} to signal that this factory cannot handle the type.
   *
   * @param gson the {@link Gson} instance requesting the adapter
   * @param type the type token describing the requested type
   * @param <T> the type to adapt
   * @return a {@link TypeAdapter} for {@link Problem}, or {@code null} if not applicable
   * @since 1.0.0
   */
  @Override
  @SuppressWarnings("unchecked")
  public @Nullable <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (!Problem.class.isAssignableFrom(type.getRawType())) {
      return null;
    }
    return (TypeAdapter<T>) new ProblemTypeAdapter(gson);
  }
}
