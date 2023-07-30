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

* DONE: Data scripts: Setup locations
* DONE: Data scripts: Setup event organizers
* DONE: Data scripts: Setup events
* DONE: Data scripts: Setup workshops
* Mock up CSV generator workflow using new DDB items
* Handler works and can call correct services

## TODO

* Where are we getting blackout dates for a location from? Request? DDB?
* How are we getting the generated CSV to person? S3 link? Immediate stream back from API?

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
