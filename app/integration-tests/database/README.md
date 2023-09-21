# Data access patterns

## Client

- Retrieve a client
- Retrieve all clients

## Location

- Retrieve a location of a client
- Retrieve all locations of a client

## Organizer

- Retrieve an organizer of a client
- Retrieve all organizers of a client

## Event

- Retrieve an event of a client
- Retrieve all events of a client

## Workshop

- Retrieve a workshop of a client
- Retrieve all workshops of a client

## AWS CLI

### Create stack

```bash
aws cloudformation create-stack --stack-name service-admin-duties-beta-dynamodb --template-body file://database/cloud-formation/dynamodb.yaml
```

### Delete stack

```bash
aws cloudformation delete-stack --stack-name service-admin-duties-beta-dynamodb
```
