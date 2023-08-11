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
* DONE: CRUD working for Organizer
* DONE: CRUD working for Event
* DONE: Figure out the readAll of Events and push to other controllers
* DONE: Create CSV controller and call base Orc service
* DONE: Flush out Orc service
* Work on validating the CSV
* Figure out how Workshops fit into Orc Service
* CRUD working for Workshop
* Remove blackout dates for location
* Remove blackout dates for event
* Set DDB config to work
* Create AOC Annotation for logging

## TODO

* Where are we getting blackout dates for a location from? Request? DDB?
* How are we getting the generated CSV to person? S3 link? Immediate stream back from API?
* Process to clear out all data if a client is deleted from CLIENT table
* How are we handling if and event is active?
* How are we handling switching an organizer midway through a month?

## Helpful Information
