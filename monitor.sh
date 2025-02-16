#!/bin/bash

set -e  # Exit on error

# API endpoint to monitor
API_URL="http://localhost/actuator/health"
LOG_FILE="api_monitor.log"

echo "📡 Monitoring API ($API_URL)... Press Ctrl+C to stop."

# Loop indefinitely to monitor API response
while true; do
  RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $API_URL)

  # Log API response with timestamp
  echo "$(date) - API Status: $RESPONSE" | tee -a $LOG_FILE

  # Check for downtime (non-200 response)
  if [ "$RESPONSE" -ne 200 ]; then
    echo "🚨 API DOWN! Status: $RESPONSE"
  fi

  sleep 1  # Wait 1 second before the next request
done
