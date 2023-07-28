import datetime
import logging
from datetime import timedelta

from administrative_duties.events.utils import csv_generator
from administrative_duties.events.utils.csv_data_factory import CsvDataFactory
from daos.events_dao import EventsDao
from models.calendar_event import CalendarEvent
from utils import date_factory_util, date_validation_util

logging.basicConfig(level=logging.INFO, format='%(message)s')
logger = logging.getLogger()

# Workflow
##############################
# Accept year and month

year: int = 2023
month: int = 7

# Validate the year and month
date_validation_util.validate_year(year)
date_validation_util.validate_month(month)

# Find the beginning and end dates of the year and month combo
month_start_date = date_factory_util.get_date_first_of_month(year, month)
# month_end_date = date_factory_util.get_date_last_of_month(year, month)
month_end_date = datetime.date(year, month, 4)

# Flush out the dates of the month with their day i.e. Monday, Tuesday
number_of_dates_delta = month_end_date - month_start_date
dates = []

for i in range(number_of_dates_delta.days + 1):
    day = month_start_date + timedelta(days=i)
    dates.append(day)

# Accept Venue Name
# Retrieve Venue Name
# Remove the blackout dates for a Venue Name
# Get list of events
events = EventsDao.get_events()
# Loop across the dates of the month, check what day it is, add the events of that day to that date

calendar_events = []

for date in dates:
    for event in events:
        if event.day_of_week == date.isoweekday():
            calendar_event = CalendarEvent()
            calendar_event.start_date = date.strftime("%Y-%m-%d")
            calendar_event.end_date = date.strftime("%Y-%m-%d")
            calendar_event.event = event

            calendar_event.venue_name = "Body Kinect"
            calendar_events.append(calendar_event)

# print(calendar_events)

# Remove the blackout dates for an event
# Add event organizer to the list of events
# Prep data for CSV
# Push the Headers
header_row = ["Event Organizer Name", "Event Start Date", "Event Start Time", "Event Length", "Event End Date",
              "Event End Time", "Event Name", "Event Category", "Event Venue Name", "Event Description"]
# Push the rows of data
table_data = []

row_data_locations = [
    "event.organizer_name",
    "start_date",
    "event.start_time",
    "event.duration_time",
    "end_date",
    "event.name",
    "event.category",
    "venue_name",
    "event.description"
]

for calendar_event in calendar_events:
    row_data = []
    for data_location in row_data_locations:
        row_data.append(CsvDataFactory.csv_row_generator(calendar_event, data_location))
    table_data.append(row_data)
# Create CSV
print(table_data)
csv_generator.create_csv("test-01.csv", header_row, table_data)
