class EventOrganizer:
    client_id: str
    organizer_id: str
    organizer_name: str

    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def __getitem__(self, item):
        return getattr(self, item)
