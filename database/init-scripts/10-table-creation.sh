aws configure set aws_access_key_id "access_key_id"
aws configure set aws_secret_access_key "secret_access_key"
aws configure set region "us-east-1"

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name CLIENT \
--attribute-definitions \
  AttributeName=client_id,AttributeType=S  \
--key-schema \
  AttributeName=client_id,KeyType=HASH  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name LOCATION \
--attribute-definitions \
  AttributeName=client_id,AttributeType=S  \
  AttributeName=location_id,AttributeType=S  \
--key-schema \
  AttributeName=client_id,KeyType=HASH  \
  AttributeName=location_id,KeyType=RANGE  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;


aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name EVENT_ORGANIZER \
--attribute-definitions \
  AttributeName=client_id,AttributeType=S  \
  AttributeName=organizer_id,AttributeType=S  \
--key-schema \
  AttributeName=client_id,KeyType=HASH  \
  AttributeName=organizer_id,KeyType=RANGE  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name EVENT \
--attribute-definitions \
  AttributeName=client_id,AttributeType=S  \
  AttributeName=event_id,AttributeType=S  \
--key-schema \
  AttributeName=client_id,KeyType=HASH  \
  AttributeName=event_id,KeyType=RANGE  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name WORKSHOP \
--attribute-definitions \
  AttributeName=client_id,AttributeType=S  \
  AttributeName=workshop_id,AttributeType=S  \
--key-schema \
  AttributeName=client_id,KeyType=HASH  \
  AttributeName=workshop_id,KeyType=RANGE  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;


sleep 2