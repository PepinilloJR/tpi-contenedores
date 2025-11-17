#!/bin/bash

SERVICES=(
  "gateway"
  "pedidos-service"
  "camiones-service"
  "depositos-service"
)

for service in "${SERVICES[@]}"; do
    echo "=============================="
    echo "BUILDING: $service"
    echo "=============================="

    cd "$service" || exit

    mvn clean package -DskipTests

    docker build -t "pepinillojr/tpi-backend:$service" .

    cd ..
done
