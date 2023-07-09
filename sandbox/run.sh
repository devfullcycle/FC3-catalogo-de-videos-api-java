# Criar as docker networks
docker network create elastic

# Criar os docker volumes
docker volume create es01

# Criar as pastas com permissões
docker compose -f elk/docker-compose.yml up -d elasticsearch