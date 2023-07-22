import calendar
import logging
from datetime import date

from utils import date_validation_util

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

# Find the beginning and end of the year and month combo
dates_of_month_range = calendar.monthrange(year, month)
print(dates_of_month_range)
month_start_date = date(year, month, 1)
print(month_start_date)
month_end_date = date(year, month, dates_of_month_range[1])
print(month_end_date)
# Flush out the dates of the month with their day i.e. Monday, Tuesday

dates = []

# Accept location
# Retrieve location
# Remove the blackout dates for a location
# Get list of events for a location with their day they occur on i.e. Monday, Tuesday
# Loop across the dates of the month, check what day it is, add the events of that day to that date
# Remove the blackout dates for an event
# Add event organizer to the list of events
# Create CSV
