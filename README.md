# codex-sample
OpenAIのCodexがPlusに配信されたので試しに使ってみる

## Backend
- Java Spring Boot application located in `backend`

## Frontend
- Static React application located in `frontend/public`

## Running with Docker
Build and start both frontend and backend containers with:

```bash
docker compose up --build
```

The backend will be available at <http://localhost:8080> and the frontend at <http://localhost:3000>.

## Preparing Maven Dependencies

For environments without internet access or simply to speed up builds, you can
pre-populate a local Maven repository with all required artifacts.

### Using Maven directly

```bash
cd backend
mvn -B dependency:go-offline
```

The above command will download dependencies into your local `~/.m2` directory.
Subsequent builds will reuse those artifacts even when offline.

### Using the provided script

Run the helper script to cache dependencies inside a local `maven_cache` folder
using a Docker container:

```bash
./scripts/preload-maven-deps.sh
```

You can then point Maven at this directory with `-Dmaven.repo.local=maven_cache`
when building the project or adapting the `Dockerfile.backend`.
