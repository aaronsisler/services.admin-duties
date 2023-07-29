def main(event, _context):
    try:
        print(event["taco"])
        msg_out = 'Hello you!'

        return {'message': msg_out}
    except Exception as e:
        return {'exception': e}
