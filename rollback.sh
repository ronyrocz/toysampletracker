#!/bin/bash

# Set service names
BLUE="toysampletracker_blue"
GREEN="toysampletracker_green"

# Get the rollback version from command-line argument (if provided)
if [ -n "$1" ]; then
    ROLLBACK_VERSION="$1"
else
    # If no version is specified, rollback to the last stable version
    if [ ! -f deployed_version_history.txt ]; then
        echo "No previous deployment history found! Specify a version to rollback to."
        exit 1
    fi

    #Pick the second-to-last deployed version (Last Stable)
    ROLLBACK_VERSION=$(tail -n 2 deployed_version_history.txt | head -n 1)
fi

IMAGE_NAME="toysampletracker:$ROLLBACK_VERSION"

echo "Rolling back to version: $ROLLBACK_VERSION"

# Check which version is currently running
if docker ps | grep -q "$BLUE"; then
    ACTIVE=$BLUE
    INACTIVE=$GREEN
else
    ACTIVE=$GREEN
    INACTIVE=$BLUE
fi

echo "Current active version: $ACTIVE"
echo "Switching back to: $INACTIVE"

# Check if the requested version exists
if ! docker image inspect $IMAGE_NAME >/dev/null 2>&1; then
    echo "Error: Image $IMAGE_NAME not found! Ensure the version exists."
    exit 1
fi

# Start the rollback version
docker compose up -d --no-deps $INACTIVE

# Wait for rollback version to be ready
echo "Waiting for rollback version to be ready..."
sleep 10

# Switch Nginx traffic back to the rollback version
echo "Switching traffic back to $INACTIVE..."
sed -i '' "s/$ACTIVE:8080;/$ACTIVE:8080 down;/" nginx.conf
sed -i '' "s/$INACTIVE:8081 down;/$INACTIVE:8081;/" nginx.conf

# Reload Nginx configuration
docker exec -it nginx-proxy nginx -s reload

echo "Rollback successful! Now serving traffic on: $INACTIVE"

# Remove the faulty version after confirming rollback is successful
echo "Removing failed version ($ACTIVE)..."
docker stop $ACTIVE && docker rm $ACTIVE

# Update the stable version record
echo "$ROLLBACK_VERSION" > deployed_version.txt
