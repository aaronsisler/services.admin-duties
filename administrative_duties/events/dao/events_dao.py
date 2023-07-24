import json

from administrative_duties.events.models.event import Event


class EventsDao:

    @staticmethod
    def get_events():
        events = []
        with open("../resources/test/events.json", "r") as f:
            raw_events = json.load(f)

        for raw_event in raw_events:
            # TODO Handle event categories
            event = Event()
            event.name = raw_event["name"]
            event.organizer_name = raw_event["organizer_name"]
            event.category = raw_event["category"]
            event.description = raw_event["description"]
            event.day_of_week = raw_event["day_of_week"]
            event.start_time = raw_event["start_time"]
            event.duration_time = raw_event["duration_time"]

            events.append(event)

        return events
