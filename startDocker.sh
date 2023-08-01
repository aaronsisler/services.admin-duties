cd app
./gradlew clean build
cd ..
docker compose -f ./docker-compose.yml up -d