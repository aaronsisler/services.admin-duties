# Start DynamoDB
# Load the data for tests
# docker compose -f ./app/integration-tests/docker-compose.yaml up -d
# Start the app somehow so endpoints work
# Run ./gradle clean test

# Can we use this to build the Docker image that can then be run?
# ./gradlew clean dockerBuild


docker compose -f ./app/integration-tests/docker-compose.yaml up -d
# docker compose -f ./app/integration-tests/docker-compose.yaml down
