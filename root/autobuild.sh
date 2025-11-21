#!/bin/bash

SERVICES=(
  "gateway"
  "pedidos-service"
  "camiones-service"
  "depositos-service"
  "commonlib"
)

echo "=== Checking Docker login using config.json ==="
if grep -q "index.docker.io" ~/.docker/config.json 2>/dev/null; then
    echo "Estás logueado en Docker Hub"
    LOGGED_IN=true
else
    echo "No estás logueado en Docker Hub"
    echo "No se subiran las imagenes"
    LOGGED_IN=false
fi


for service in "${SERVICES[@]}"; do
    echo "=============================="
    echo "BUILDING: $service"
    echo "=============================="

    cd "$service" || exit

    mvn clean package -DskipTests

    docker build -t "pepinillojr/tpi-backend:$service" .

    if [ "$LOGGED_IN" = true ]; then
        echo "Haciendo push de pepinillojr/tpi-backend:$service"
        docker push "pepinillojr/tpi-backend:$service"
    else
        echo "Saltando push"
    fi


    cd ..
done

docker image prune -f
