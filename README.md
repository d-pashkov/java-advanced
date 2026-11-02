# Java Advanced Coursework

Selected completed solutions for ITMO's Java Advanced course. The code follows the course package layout and implements the corresponding course interfaces.

## Topics

| Area | Implementation | Focus |
| --- | --- | --- |
| File walking | `walk/` | Recursive traversal and rolling-hash checksums |
| Parallel programming | `iterative/` | Parallel collection operations and a parallel mapper |
| UDP | `hello/` | Blocking and non-blocking clients and servers |
| Web crawling | `crawler/` | Concurrent crawling with per-host limits |
| Internationalization | `i18n/` | Locale-aware text statistics and report rendering |
| RMI | `bank/` | Local and remote persons, accounts, and bank service |

## Build

Requires JDK 21 or newer. Validation used Amazon Corretto 21.0.10 (`javac 21.0.10`). The build also needs the official Java Advanced artifacts and jsoup; these dependencies are not included. Clone the [official course repository](https://www.kgeorgiy.info/git/geo/java-advanced-2026), whose `artifacts/` and `lib/` directories contain the needed JARs. In particular, the selected implementations use `info.kgeorgiy.java.advanced.base.jar`, `crawler.jar`, `hello.jar`, `iterative.jar`, and `mapper.jar` from `artifacts/`, plus `jsoup-1.8.1.jar` from `lib/`. Set `COURSE_LIB_DIR` to the cloned repository root. The course's [homework page](https://www.kgeorgiy.info/courses/java-advanced/homeworks.html) describes the assignments.

```sh
COURSE_LIB_DIR=/path/to/java-advanced-2026 ./compile.sh
```

The script compiles the selected student sources and copies the `.properties` resource bundles into `build/classes` at their package paths. At runtime, include the JARs under the course repository's `artifacts/` and `lib/` directories on the classpath. Course checker suites are not included in this portfolio export.

## Validation

All 28 Java source files compiled against official course interface artifacts and jsoup, without copying those dependencies into this repository. The text statistics CLI ran with both locale bundles loaded from the compiled classpath. Blocking and non-blocking UDP client/server combinations also passed localhost request-response smoke checks.

## Provenance and attribution

Implementation sources are by Dmitriy Pashkov and were selected from his completed ITMO coursework. Assignment requirements and interface types come from the Java Advanced course maintained by Georgiy Korneev and ITMO. This export does not include instructor test code, course JARs, private repository metadata, or generated files. Course-provided names and interfaces remain attributed to their owners.
