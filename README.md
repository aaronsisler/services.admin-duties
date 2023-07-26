# services.administrative-duties

## Running Locally

Start the Docker containers

```bash
docker compose -f ./database/docker-compose.local.yml up -d
```

List out the tables created

```bash
aws dynamodb list-tables
aws --endpoint-url http://localhost:8000 dynamodb list-tables
```

Stop the Docker containers

```bash
docker compose -f ./database/docker-compose.local.yml down
```
