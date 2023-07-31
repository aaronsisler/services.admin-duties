cd app
./gradlew clean build
cd ..
docker compose -f ./docker-compose.local.yml up -d