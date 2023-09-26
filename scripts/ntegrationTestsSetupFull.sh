# Clean up by tearing down all containers
docker compose -f ./app/integration-tests/docker-compose.yaml down

# Start DynamoDB and load the data for tests
docker compose -f ./app/integration-tests/docker-compose.yaml up -d

# Start the app
cd app
MICRONAUT_ENVIRONMENTS=local ./gradlew clean run
