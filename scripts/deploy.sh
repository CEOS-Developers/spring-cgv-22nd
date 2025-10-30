#!/bin/bash
set -e

if [ -f .env ]; then
  export $(cat .env | xargs)
fi

IMAGE_NAME="hxxukii/ceos-cgv-server:latest"
APP_CONTAINER="cgv-app"
REDIS_CONTAINER="cgv-redis"
NETWORK_NAME="cgv-network"

docker network create $NETWORK_NAME 2>/dev/null || true

docker pull $IMAGE_NAME

if [ ! "$(docker ps -q -f name=$REDIS_CONTAINER)" ]; then
  docker run -d \
    --name $REDIS_CONTAINER \
    --network $NETWORK_NAME \
    -p 6379:6379 \
    --restart unless-stopped \
    redis:7-alpine 2>/dev/null || docker start $REDIS_CONTAINER
fi

docker stop $APP_CONTAINER 2>/dev/null || true
docker rm $APP_CONTAINER 2>/dev/null || true

# 새 앱 컨테이너 실행
docker run -d \
  --name $APP_CONTAINER \
  --network $NETWORK_NAME \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e DB_HOST=${DB_HOST} \
  -e DB_PORT=${DB_PORT} \
  -e DB_NAME=${DB_NAME} \
  -e DB_USERNAME=${DB_USERNAME} \
  -e DB_PASSWORD=${DB_PASSWORD} \
  -e REDIS_HOST=$REDIS_CONTAINER \
  -e REDIS_PORT=6379 \
  -e JWT_SECRET=${JWT_SECRET} \
  -e PAYMENT_API_SECRET=${PAYMENT_API_SECRET} \
  -e PAYMENT_STORE_ID=${PAYMENT_STORE_ID} \
  --restart unless-stopped \
  $IMAGE_NAME

sleep 15

for i in {1..10}; do
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health 2>/dev/null || echo "000")

  if [ "$HTTP_STATUS" = "200" ]; then
    echo "Deployment succeeded"
    docker image prune -af --filter "until=24h" 2>/dev/null || true
    exit 0
  fi

  echo "Waiting... ($i/10)"
  sleep 5
done

echo "Deployment failed Rolling back"
docker stop $APP_CONTAINER 2>/dev/null || true
docker rm $APP_CONTAINER 2>/dev/null || true
exit 1
