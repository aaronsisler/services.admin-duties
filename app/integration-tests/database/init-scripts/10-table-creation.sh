aws configure set aws_access_key_id "access_key_id"
aws configure set aws_secret_access_key "secret_access_key"
aws configure set region "us-east-1"

aws dynamodb create-table \
--endpoint-url=http://dynamo-db-local:8000 \
--table-name SERVICES_ADMIN_DUTIES_TESTS \
--attribute-definitions \
  AttributeName=partitionKey,AttributeType=S  \
  AttributeName=sortKey,AttributeType=S \
--key-schema \
  AttributeName=partitionKey,KeyType=HASH \
  AttributeName=sortKey,KeyType=RANGE \
--billing-mode PAY_PER_REQUEST;

sleep 2

# aws cloudformation create-stack \
# --endpoint-url=http://dynamo-db-local:8000 \
# --stack-name service-admin-duties-tests-dynamodb \
# --template-body file://cloud-formation/dynamodb.yaml