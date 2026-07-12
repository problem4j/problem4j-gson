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

import org.jspecify.annotations.NullMarked;

/**
 * Gson integration module for RFC 7807 Problem objects.
 *
 * <p>This module provides Gson integration for serializing and deserializing RFC 7807 Problem
 * objects. It declares an optional dependency on Gson, making it optional for module resolution.
 *
 * <p>The module exports the {@code io.github.problem4j.gson} package and provides the {@code
 * ProblemTypeAdapterFactory} for registering with a Gson instance.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
 * @see io.github.problem4j.gson.ProblemTypeAdapterFactory
 */
@NullMarked
module io.github.problem4j.gson {
  requires static com.google.gson;
  requires static org.jspecify;
  requires transitive io.github.problem4j.core;

  exports io.github.problem4j.gson;
}
