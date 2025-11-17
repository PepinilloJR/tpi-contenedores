#!/bin/bash

POSTGRES_SERVICE_NAME="db"        # nombre del servicio en docker-compose.yml
CONTAINER_NAME="compose-docker-db-1"           # el nombre real del contenedor que crea Docker
DB_NAME="mydatabasename"
DB_USER="postgres"

echo "========================================"
echo "1) Levantando solo POSTGRES..."
echo "========================================"
docker compose up -d $POSTGRES_SERVICE_NAME

echo "Esperando a que Postgres esté listo..."
until docker exec -i $CONTAINER_NAME pg_isready -U $DB_USER >/dev/null 2>&1; do
    sleep 1
done

echo "========================================"
echo "2) Reiniciando base de datos '$DB_NAME'..."
echo "========================================"

docker exec -i $CONTAINER_NAME psql -U $DB_USER <<EOF
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = '$DB_NAME';

DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME;
EOF

echo "Base reiniciada."

echo "========================================"
echo "3) Levantando TODOS los servicios"
echo "========================================"
docker compose up --build

echo "========================================"
echo "Ambiente levantado con DB limpia"
echo "========================================"
