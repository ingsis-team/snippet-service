
# SnippetService

## Overview

SnippetService is a Spring Boot-based application for managing and executing code snippets. This project leverages Gradle as the build tool, PostgreSQL as the database, and Redis for caching. The application is containerized using Docker.

## Prerequisites

- Docker and Docker Compose installed on your machine
- Java 17 or later installed
- Gradle 8.5 or later installed

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/SnippetService.git
cd SnippetService
```

### Step 2: Environment Variables

Create a `.env` file in the project root with the following variables:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=yourpassword
POSTGRES_DB=snippet_db
DB_HOST=db
DB_PORT=5432
```

### Step 3: Running with Docker Compose

Ensure your Docker environment is running. Use the following commands to build and run the application:

```bash
docker-compose up --build
```

This will build the Docker images and start the services, including the PostgreSQL database and Redis.

### Step 4: Access the Application

The application will be available on:

```plaintext
http://localhost:8083
```

### Step 5: Running Tests

You can run the tests by executing the following command inside the project directory:

```bash
./gradlew test
```

### Step 6: Ktlint Code Formatting

Make sure your Kotlin code is properly formatted. You can run ktlint as follows:

```bash
./gradlew ktlintFormat
```

### Step 7: Stopping the Application

To stop the application and its services:

```bash
docker-compose down
```

### Step 8: Cleaning Up Docker Containers

To remove all Docker containers, networks, and volumes created by Docker Compose:

```bash
docker-compose down --volumes
```

## Project Structure

```plaintext
├── src/main/kotlin
│   ├── snippet
│   │   ├── SnippetServiceApplication.kt  # Main entry point of the application
│   │   ├── controller                    # REST controllers
│   │   ├── service                       # Business logic services
│   │   └── repository                    # Data repositories for PostgreSQL
├── docker-compose.yml                    # Docker Compose configuration
├── build.gradle.kts                      # Gradle build script
└── README.md                             # This file
```

## Technologies

- **Kotlin**: Main language for the service
- **Spring Boot**: Backend framework for developing microservices
- **PostgreSQL**: Relational database
- **Redis**: In-memory data store used for caching
- **Docker**: For containerizing the application
- **Gradle**: Build tool

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
