# services.admin-duties

## Task List

https://github.com/users/aaronsisler/projects/9/views/1

## Running Locally

Start the Docker containers

```bash
docker compose -f ./docker-compose.local.yml up -d
```

List out the tables created

**Note** There is an alias assumed if using the `awslocal` command below. The alias assumes you have set the following:

```
awslocal=aws --endpoint-url http://localhost:8000
```

```bash
awslocal dynamodb list-tables
```

List out data in a table

```bash
awslocal dynamodb scan --table-name CLIENT
```

Stop the Docker containers

```bash
docker compose -f ./docker-compose.local.yml down
```

## Testing

```bash
docker-compose -f ./app/integration-tests/docker-compose.yaml up -d
```

## Helpful Information

### Monthly Schedule Update

1. Add the events to the Monthly Upload Google Sheet from the picture Tami sends in text
2. Update the database/data/40_events.json to be correct
3. TODO below
4. Start up the docker
5. Change the endpoint year and month
6. Call the endpoint
7. Get the CSV from build/resources/main/output