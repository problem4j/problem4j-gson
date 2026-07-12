# Agent Instructions - `problem4j-gson`

Gson integration for [problem4j-core](https://github.com/problem4j/problem4j-core) - serialization and
deserialization of RFC 7807/9457 Problem objects.

## Module

Single module. Entry points:

- [`ProblemTypeAdapterFactory`](src/main/java/io/github/problem4j/gson/ProblemTypeAdapterFactory.java),
- [`ProblemTypeAdapter`](src/main/java/io/github/problem4j/gson/ProblemTypeAdapter.java).

`problem4j-core` and `gson` are `compileOnly`/`testImplementation` (consumers bring their own). Java 8 baseline, with
a multi-release `src/main9/java/module-info.java` for the module path (Java 9+).

## Build & Validate

Requires **JDK 17+** for the Gradle runtime; code compiles to Java 8 bytecode. Dependencies managed in
`gradle/libs.versions.toml`. Custom Gradle plugins live in `build-logic`.

```shell
./gradlew                  # default: spotlessApply build (preferred)
./gradlew spotlessApply    # auto-format code
./gradlew build            # compile + test + spotlessCheck
./gradlew test             # tests only
```

Always validate changes with a full `./gradlew` run before considering the task complete. If Spotless fails, run
`./gradlew spotlessApply` first, then re-run `./gradlew`.

## Agent Rules

- Do not use terminal commands (e.g., `cat`, `find`, `ls`) to read or list project files - use IDE/agent tools instead.
- Run tests once, save output to `build/test-run.log` inside the repo (`> build/test-run.log 2>&1`), then read from that
  file to extract errors. Never run the same test command multiple times, without changes in sources. Store test output
  in multiple files if you want to compare before/after changes (ex. `build/test-run-{i}.log`).

## Coding Rules

- No self-explaining comments - only add comments for non-obvious context.
- No wildcard imports.
- Follow existing code patterns and naming conventions.
- Let `spotlessApply` handle all formatting - never format manually.

## Test Conventions

- Method naming: `givenThis_whenThat_thenWhat`.
- No `// given`, `// when`, `// then` section comments.
- Cover both positive and negative cases.
