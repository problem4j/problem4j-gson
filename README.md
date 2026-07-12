# Problem4J Gson

[![Build Status](https://github.com/problem4j/problem4j-gson/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/problem4j/problem4j-gson/actions/workflows/gradle-build.yml)
[![Sonatype](https://img.shields.io/maven-central/v/io.github.problem4j/problem4j-gson)][maven-central]
[![License](https://img.shields.io/github/license/problem4j/problem4j-gson)](https://github.com/problem4j/problem4j-gson/blob/main/LICENSE)

Gson integration module for [`problem4j-core`][problem4j-core]. Provides easy support for serializing and deserializing
the `Problem` model using [Gson][gson]'s `GsonBuilder`.

> Note that [RFC 7807][rfc7807] was later extended in [RFC 9457][rfc9457], however core concepts remain the same.

## Table of Contents

- [Features](#features)
- [Example](#example)
- [Usage](#usage)
- [Project Status](#project-status)
- [Problem4J Links](#problem4j-links)
- [Building from source](#building-from-source)

## Features

- Seamless JSON serialization of `Problem` objects.
- Compatible with standard Gson `GsonBuilder`.
- Pluggable via Gson's `TypeAdapterFactory` mechanism.
- Lightweight, with no external dependencies beyond Gson and `problem4j-core`.
- Supports **Java Platform Module System**. Artifact uses multi-release JAR to support Java 8 and Java 9+ module system.

## Example

Serialize and deserialize a `Problem` object using Gson.

```java
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.problem4j.core.Problem;
import io.github.problem4j.gson.ProblemTypeAdapterFactory;

Gson gson = new GsonBuilder()
    .registerTypeAdapterFactory(new ProblemTypeAdapterFactory())
    .create();

Problem problem =
    Problem.builder()
        .title("Bad Request")
        .status(400)
        .detail("not a valid json")
        .build();

String json = gson.toJson(problem, Problem.class);
Problem parsed = gson.fromJson(json, Problem.class);
```

If using Java module system, add the following `requires` directive to your `module-info.java` file.

```java
module org.example.project {
    requires io.github.problem4j.gson;
}
```

## Usage

Add library as dependency to Maven or Gradle. See the actual versions on [Maven Central][maven-central]. **Java 8** or
higher is required to use this library.

> [!IMPORTANT]
> The `problem4j-gson` module does **not** declare `gson` as a transitive dependency. You must include `problem4j-core`
> as a dependency in your project.

[![Sonatype](https://img.shields.io/maven-central/v/io.github.problem4j/problem4j-gson)][maven-central]

1. Maven:
   ```xml
   <dependencies>
       <dependency>
           <groupId>com.google.code.gson</groupId>
           <artifactId>gson</artifactId>
           <version>2.14.0</version>
       </dependency>
       <dependency>
           <groupId>io.github.problem4j</groupId>
           <artifactId>problem4j-core</artifactId>
           <version>2.0.0</version>
       </dependency>
       <dependency>
           <groupId>io.github.problem4j</groupId>
           <artifactId>problem4j-gson</artifactId>
           <version>{version}</version>
       </dependency>
   </dependencies>
   ```
2. Gradle (Groovy or Kotlin DSL):
   ```groovy
   dependencies {
       implementation("com.google.code.gson:gson:2.14.0")
       implementation("io.github.problem4j:problem4j-core:2.0.0")
       implementation("io.github.problem4j:problem4j-gson:{version}")
   }
   ```

## Project Status

[![Status: Feature Complete](https://img.shields.io/badge/feature%20complete-darkblue?label=status)](#project-status)

**Problem4J Gson** is considered *feature complete*. Only **bug fixes** will be added. New features may be included only
if there is a strong justification for them; otherwise, future projects are expected to build on this one as a
dependency.

## Problem4J Links

- [`problem4j.github.io`](https://problem4j.github.io) - Full documentation of all projects from Problem4J family.
- [`problem4j-core`][problem4j-core] - Core library defining `Problem` model and `ProblemException`.
- [`problem4j-gson`][problem4j-gson] - Gson module for serializing and deserializing `Problem` objects.
- [`problem4j-jackson`][problem4j-jackson] - Jackson module for serializing and deserializing `Problem` objects.
- [`problem4j-spring`][problem4j-spring] - Spring modules extending `ResponseEntityExceptionHandler` for handling
  exceptions and returning `Problem` responses.

## Building from source

<details>
<summary><b>Expand...</b></summary>

Gradle **9.x+** requires **Java 17** or higher to run. For building the project, Gradle automatically picks up **Java
25** via **toolchains** and the `foojay-resolver-convention` plugin. This Java version is needed because the project
uses **ErrorProne** and **NullAway** for static nullness analysis.

The produced artifacts are compatible with **Java 8** thanks to `options.release = 8` in the Gradle `JavaCompile` task.
This means that regardless of the Java version used to run Gradle, the resulting bytecode remains compatible.

The **default Gradle tasks** include `spotlessApply` (for code formatting) and `build` (for compilation and tests). The
simplest way to build the project is to run:

```bash
./gradlew
```

---

To **execute tests** use `test` task. Tests do not change `options.release` so newer Java API can be used.

```bash
./gradlew test
```

---

To **format the code** according to the style defined in [`build.gradle.kts`](./build.gradle.kts) rules use `spotlessApply`
task. **Note** that **building will fail** if code is not properly formatted.

```bash
./gradlew spotlessApply
```

**Note** that if the year has changed, add `-Pspotless.license-year-enabled` flag to update the year in license headers.
The [publishing GitHub Action](.github/workflows/gradle-publish-release.yml) will fail if the year is not updated, but
for local development and builds you can choose to skip it and update the year later.

```bash
./gradlew spotlessApply -Pspotless.license-year-enabled
```

---

To **publish** the built artifacts to **local Maven repository**, use `publishToMavenLocal` task.

```bash
./gradlew publishToMavenLocal
```

Note that for using Maven Local artifacts in target projects, you need to add `mavenLocal()` repository.

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}
```

---

**Note** that the following warning during build is expected and can be ignored. It appears because the module-info is
compiled against the main sources without Gson on the module path:

```txt
> Task :compileMain9Java
./src/main9/java/module-info.java: warning: [module] module not found: com.google.gson
```

</details>

[gson]: https://github.com/google/gson

[maven-central]: https://central.sonatype.com/artifact/io.github.problem4j/problem4j-gson

[problem4j-core]: https://github.com/problem4j/problem4j-core

[problem4j-jackson]: https://github.com/problem4j/problem4j-jackson

[problem4j-gson]: https://github.com/problem4j/problem4j-gson

[problem4j-spring]: https://github.com/problem4j/problem4j-spring

[rfc7807]: https://datatracker.ietf.org/doc/html/rfc7807

[rfc9457]: https://datatracker.ietf.org/doc/html/rfc9457
