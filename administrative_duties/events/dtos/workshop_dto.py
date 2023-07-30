class WorkshopDto:
    name: str
    organizer_name: str
    category: str
    description: str
    workshop_date: str
    start_time: str
    duration_time: str
    cost: int

    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def __getitem__(self, item):
        return getattr(self, item)
