import boto3


class ClientDao:
    CLIENT_TABLE_NAME = "CLIENT"

    @staticmethod
    def read(self, client_id: str):
        dynamodb = boto3.client("dynamodb")
        client_table = dynamodb.Taable(self.CLIENT_TABLE_NAME)
        client_table.get_item(Key={
            'artist': {'S': 'Arturus Ardvarkian'},
        })
