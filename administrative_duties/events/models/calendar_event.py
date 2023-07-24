from .event import Event


class CalendarEvent(dict):
    event: Event
    start_date: str
    end_date: str
    venue_name: str

    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def __getitem__(self, item):
        return getattr(self, item)
