# API Testing Framework

Java-based API testing framework using RestAssured, JUnit 5, and Allure.

## Features

- REST API testing with RestAssured
- Multiple environment support (dev, test, prod)
- Allure test reporting
- Parallel test execution
- Structured logging
- Error handling and retry mechanisms
- Test data management

## Requirements

- Java 21
- Gradle 9.0+

## Setup

1. Clone the repository
2. Configure environment variables or edit `app/src/test/resources/config.yaml`
3. Run tests: `./gradlew test`
4. Generate Allure report: `./gradlew allureReport`

## Configuration

Edit `app/src/test/resources/config.yaml` to configure:
- Base URL
- Authentication token
- Timeout settings
- Active profile

## Running Tests

```bash
./gradlew test
```

Run specific test tags:
```bash
./gradlew test --tests "*ContactTests" -PtestTag=smoke
```

## Project Structure

```
app/
├── src/
│   ├── main/java/demo/api/
│   │   ├── client/          # HTTP client and handlers
│   │   ├── config/          # Configuration management
│   │   ├── constants/       # Constants
│   │   ├── dto/             # Data transfer objects
│   │   ├── exceptions/      # Custom exceptions
│   │   ├── services/        # API service layer
│   │   └── validators/      # Response validators
│   └── test/java/demo/api/
│       ├── tests/           # Test classes
│       └── testdata/         # Test data generators and managers
```

## CI/CD

GitHub Actions workflow is configured in `.github/workflows/ci.yml`

## License

MIT

