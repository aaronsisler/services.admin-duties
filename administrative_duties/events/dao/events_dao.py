import json


class EventsDao:

    @staticmethod
    def get_events():
        with open("../resources/test/events.json", "r") as f:
            events = json.load(f)
            print(events)
