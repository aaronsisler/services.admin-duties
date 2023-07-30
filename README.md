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

## Task List

* DONE: Data scripts: Get locations setup
* DONE: Data scripts: Get event organizers setup
* Data scripts: Get events setup
* Handler works and can call correct services

## Helpful Information

### datetime.isoweekday()

| Number | Day       |
|--------|-----------|
| 1      | Monday    |
| 2      | Tuesday   |
| 3      | Wednesday |
| 4      | Thursday  |
| 5      | Friday    |
| 6      | Saturday  |
| 7      | Sunday    |
