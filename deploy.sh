#!/bin/bash

set -e  # Exit immediately if a command fails

# Define service names
APP_CONTAINER="toysampletracker"
NGINX_CONTAINER="nginx-proxy"

# Get the latest version from `pom.xml`
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
IMAGE_NAME="toysampletracker:$VERSION"

echo "Deploying version: $VERSION"

# Ensure Maven builds the correct JAR file
echo "Building new application..."
mvn clean package
JAR_FILE=$(ls target/*.jar | head -n 1)

# Build and tag the new Docker image
echo "Building Docker image..."
docker build --build-arg JAR_FILE=$JAR_FILE -t $IMAGE_NAME .

# Start the new version while the old one is still running
echo "Starting new version in Docker..."
docker compose up -d --build

# Wait for the new app to become healthy before stopping the old one
echo "⏳ Waiting for the new app to be healthy..."
TIMEOUT=60  # Max wait time (seconds)
TIMER=0

until curl -s http://localhost/actuator/health | grep -q '"status":"UP"'; do
  sleep 2
  echo -n "."
  TIMER=$((TIMER+2))
  if [ $TIMER -ge $TIMEOUT ]; then
    echo "Timeout waiting for new app to be healthy. Exiting..."
    exit 1
  fi
done

echo -e "\nNew app is healthy!"

# Reload Nginx to recognize the updated container
echo "Reloading Nginx..."
docker exec $NGINX_CONTAINER nginx -s reload || echo "⚠ Warning: Nginx reload failed, check logs."

echo "Deployment successful!"