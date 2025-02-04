#!/bin/bash

# Set service names
BLUE="toysampletracker_blue"
GREEN="toysampletracker_green"

# Get the Spring Boot app version from `pom.xml`
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
IMAGE_NAME="toysampletracker:$VERSION"

echo "Deploying version: $VERSION"

# Check which version is currently running
if docker ps | grep -q "$BLUE"; then
    ACTIVE=$BLUE
    INACTIVE=$GREEN
else
    ACTIVE=$GREEN
    INACTIVE=$BLUE
fi

echo "Active version: $ACTIVE"
echo "Deploying new version ($VERSION) to: $INACTIVE"

# Ensure Maven builds the correct JAR file
mvn clean package
JAR_FILE=$(ls target/*.jar | head -n 1)  # Dynamically find the generated JAR

# Build and tag the new version
docker build --build-arg JAR_FILE=$JAR_FILE -t $IMAGE_NAME .
docker tag $IMAGE_NAME toysampletracker:latest

# Deploy the new version
docker compose up -d --no-deps $INACTIVE

# Wait for the new version to be ready
echo "Waiting for new version to be ready..."
sleep 10  # Adjust if needed

# Switch Nginx to route traffic to the new version
echo "Switching traffic to $INACTIVE..."
sed -i '' "s/$ACTIVE:8080;/$ACTIVE:8080 down;/" nginx.conf
sed -i '' "s/$INACTIVE:8081 down;/$INACTIVE:8081;/" nginx.conf

# Reload Nginx BEFORE deleting the old version
docker exec -it nginx-proxy nginx -s reload

echo "Deployment successful! Now serving traffic on: $INACTIVE"

# Wait a few seconds to ensure Nginx has switched
sleep 5

# Remove old version (after confirming traffic has switched)
echo "Removing old version ($ACTIVE)..."
docker stop $ACTIVE && docker rm $ACTIVE


# Keep track of deployed versions (Append instead of overwriting)
echo "\n$VERSION" >> deployed_version_history.txt

# Store the last stable version separately (if deployment is successful)
echo "$VERSION" > deployed_version.txt

