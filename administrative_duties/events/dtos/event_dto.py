class EventDto:
    client_id: str
    event_id: str
    location_id: str
    organizer_id: str
    name: str
    category: str
    description: str
    day_of_week: int
    start_time: str
    duration_time: str
    cost: int
    is_active: bool

    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def __getitem__(self, item):
        return getattr(self, item)
