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
