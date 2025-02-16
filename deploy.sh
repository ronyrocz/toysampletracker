#!/bin/bash

set -e  # Exit immediately if a command fails

# Set service names
BLUE="toysampletracker_blue"
GREEN="toysampletracker_green"
NGINX_CONTAINER="nginx-proxy"

# Determine the active service
if docker ps | grep -q "$BLUE"; then
    ACTIVE=$BLUE
    INACTIVE=$GREEN
else
    ACTIVE=$GREEN
    INACTIVE=$BLUE
fi

echo "✅ Active version: $ACTIVE"
echo "🔄 Deploying new version to: $INACTIVE"

# Ensure Maven builds the correct JAR file
mvn clean package
JAR_FILE=$(ls target/*.jar | head -n 1)  # Dynamically find the generated JAR

# Build and tag the new image
docker build --build-arg JAR_FILE=$JAR_FILE -t toysampletracker:latest .

# Deploy the new version to the inactive container
docker compose up -d --no-deps $INACTIVE

# Wait for the new container to become healthy
echo "⏳ Waiting for $INACTIVE to become healthy..."
TIMEOUT=60  # Max wait time (seconds)
TIMER=0

until [ "$(docker inspect -f '{{.State.Health.Status}}' $INACTIVE 2>/dev/null)" == "healthy" ]; do
  sleep 2
  echo -n "."
  TIMER=$((TIMER+2))
  if [ $TIMER -ge $TIMEOUT ]; then
    echo "❌ Timeout waiting for $INACTIVE to be healthy. Exiting..."
    exit 1
  fi
done

echo "✅ $INACTIVE is healthy!"

# Switch traffic to the new version in NGINX
echo "🔀 Switching traffic to $INACTIVE..."
sed -i.bak "s/$ACTIVE:8080;/$ACTIVE:8080 down;/" nginx.conf
sed -i.bak "s/$INACTIVE:8081 down;/$INACTIVE:8081;/" nginx.conf

# Reload NGINX without downtime
docker exec -it $NGINX_CONTAINER nginx -s reload || echo "⚠ Warning: Nginx reload failed, check logs."

echo "✅ Deployment successful! Now serving traffic on: $INACTIVE"

# Wait for a few seconds to confirm stability
sleep 10

# Remove the old version
echo "🗑 Removing old version ($ACTIVE)..."
docker stop $ACTIVE && docker rm $ACTIVE

echo "🚀 Zero-downtime deployment completed successfully!"
