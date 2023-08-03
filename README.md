# services.administrative-duties

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

## Task List

* DONE: Data scripts: Setup locations
* DONE: Data scripts: Setup event organizers
* DONE: Data scripts: Setup events
* DONE: Data scripts: Setup workshops
* DONE: Database Abstract working
* DONE: Local DDB config working
* DONE: Get C-U-D working for Client
* DONE: CRUD working for Location
* CRUD working for Organizer
* CRUD working for Event
* Figure out the readAll of Events and push to other controllers
* CRUD working for Workshop
* Flush out orchestration service
* Set DDB config to work

## TODO

* Where are we getting blackout dates for a location from? Request? DDB?
* How are we getting the generated CSV to person? S3 link? Immediate stream back from API?
* Process to clear out all data if a client is deleted from CLIENT table

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
