aws configure set aws_access_key_id "access_key_id"
aws configure set aws_secret_access_key "secret_access_key"
aws configure set region "us-east-1"

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name CLIENT \
--attribute-definitions \
AttributeName=clientId,AttributeType=S  \
--key-schema \
AttributeName=clientId,KeyType=HASH  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name LOCATION \
--attribute-definitions \
AttributeName=clientId,AttributeType=S  \
AttributeName=locationId,AttributeType=S  \
--key-schema \
AttributeName=clientId,KeyType=HASH  \
AttributeName=locationId,KeyType=RANGE  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name EVENT \
--attribute-definitions \
AttributeName=clientId,AttributeType=S  \
AttributeName=eventId,AttributeType=S  \
--key-schema \
AttributeName=clientId,KeyType=HASH  \
AttributeName=eventId,KeyType=RANGE  \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5;
