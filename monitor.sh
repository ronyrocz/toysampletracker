#!/bin/bash

set -euo pipefail  # Better error handling

API_URL="http://localhost/actuator/health"
LOG_FILE="api_monitor.log"

echo "📡 Monitoring API ($API_URL)... Press Ctrl+C to stop."

# Loop indefinitely to monitor API response
while true; do
  RESPONSE=$(curl -s --retry 5 --retry-delay 2 --connect-timeout 5 -o /dev/null -w "%{http_code}" "$API_URL" || echo "000")

  echo "$(date) - API Status: $RESPONSE" | tee -a "$LOG_FILE"

  if [ "$RESPONSE" -ne 200 ]; then
    echo "🚨 API DOWN! Status: $RESPONSE"
  fi

  sleep 1
done
