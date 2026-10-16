# Yuva Intern Week 3 — Unit Testing and Debugging

## Project
Library Book Inventory Management System (Java command-line application).

## Requirements
- JDK 8 or newer
- Apache Maven 3.6+ (needed to download JUnit and run the test suite)
- Internet access the first time Maven downloads dependencies

## Run the application
From this project folder:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=Main
```

If the Maven exec plugin is not configured locally, use the JDK directly:

```bash
mkdir out
javac -d out src/main/java/Book.java src/main/java/LibraryManager.java src/main/java/Main.java
java -cp out Main
```

## Run unit tests
```bash
mvn test
```

JUnit 5 test cases cover:
- Create, list/read, search, update and delete operations
- IDs and missing-record behavior
- Duplicate ISBN checks (including case/whitespace differences)
- Empty/blank fields and invalid publication years
- Invalid update atomicity (the original record remains unchanged)
- Defensive list-copy behavior and text trimming

## Project structure
- `src/main/java/` — application source files
- `src/test/java/LibraryManagerTest.java` — JUnit 5 tests
- `pom.xml` — Maven/JUnit configuration
- `docs/Week3_Debugging_and_Test_Report.docx` — separate report

## Notes
The application stores records in memory. Data is cleared when the process exits. The test report lists the test scenarios and the expected outcomes; run `mvn test` locally and use the actual Maven summary as the final test result. Do not report tests as passed unless you have run them successfully.
