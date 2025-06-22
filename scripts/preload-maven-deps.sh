#!/bin/sh
# Preload Maven dependencies into a local repository
#
# This script uses the official Maven Docker image to download all
# dependencies defined in the backend module. The artifacts are cached
# under the `maven_cache` directory so subsequent builds can run
# without fetching packages from the internet.

set -e

CACHE_DIR="$(pwd)/maven_cache"
mkdir -p "$CACHE_DIR"

# Run Maven in a Docker container with a bind mount for the cache
# and the backend source.
docker run --rm -v "$CACHE_DIR":/root/.m2 \
    -v "$(pwd)/backend":/app -w /app maven:3.9-eclipse-temurin-21 \
    mvn -B dependency:go-offline

echo "Dependencies cached in $CACHE_DIR"
