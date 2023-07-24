class Event:
    name: str
    organizer_name: str
    category: str
    description: str
    day_of_week: int
    start_time: str
    duration_time: str
    end_time: str

    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def __getitem__(self, item):
        return getattr(self, item)
