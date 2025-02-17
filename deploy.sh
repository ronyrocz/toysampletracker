#!/bin/bash

set -e  # Exit immediately if a command fails

# Set service names
BLUE="toysampletracker_blue"
GREEN="toysampletracker_green"
NGINX_CONTAINER="nginx-proxy"

# Determine the active and inactive instances
if docker ps | grep -q "$BLUE"; then
    ACTIVE=$BLUE
    INACTIVE=$GREEN
else
    ACTIVE=$GREEN
    INACTIVE=$BLUE
fi

echo "✅ Active instance: $ACTIVE"
echo "🔄 Deploying new version to: $INACTIVE"

# Ensure Maven builds the correct JAR file
mvn clean package
JAR_FILE=$(ls target/*.jar | head -n 1)  # Dynamically find the generated JAR

# Build and tag the new image
docker build --build-arg JAR_FILE=$JAR_FILE -t toysampletracker:latest .

# Start the new version (inactive instance)
echo "🚀 Starting new version ($INACTIVE)..."
docker compose up -d --no-deps --force-recreate $INACTIVE

# Wait for the new instance to become healthy
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

# Update upstream.conf to switch traffic to the new instance
echo "🔀 Switching traffic to $INACTIVE..."
sed -i.bak "s/server $ACTIVE:8080;/server $ACTIVE:8080 down;/" upstream.conf
sed -i.bak "s/server $INACTIVE:8080 down;/server $INACTIVE:8080;/" upstream.conf

# Copy the updated upstream.conf to a temporary location inside the container
docker cp upstream.conf nginx-proxy:/etc/nginx/upstream.conf.tmp

# Overwrite upstream.conf using cp (avoids "No such file" issue)
docker exec nginx-proxy sh -c "cp /etc/nginx/upstream.conf.tmp /etc/nginx/upstream.conf"

# Validate Nginx config and reload
docker exec nginx-proxy nginx -t && docker exec nginx-proxy nginx -s reload

echo "✅ Traffic switched to $INACTIVE"

# Stop the old instance
echo "🛑 Stopping old instance ($ACTIVE)..."
docker stop $ACTIVE || echo "⚠ Warning: $ACTIVE was already stopped."

# Remove the old instance
echo "🗑 Removing old instance ($ACTIVE)..."
docker rm $ACTIVE || echo "⚠ Warning: $ACTIVE was already removed."

# Restart the new instance to ensure it runs on the correct port
echo "🔄 Restarting $INACTIVE..."
docker compose up -d --force-recreate $INACTIVE

# Wait for final confirmation
sleep 5
echo "🚀 Zero-downtime deployment completed successfully!"