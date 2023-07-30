from administrative_duties.events.dtos.csv_generation_request import CsvGenerationRequest
from administrative_duties.events.utils import date_validation_util


class OrchestrationService:
    @staticmethod
    def orchestrate_csv_generation(self, request: CsvGenerationRequest):
        # Validate the year and month
        date_validation_util.validate_year(request.year)
        date_validation_util.validate_month(request.month)

# Workflow
# Validate the inputs
# DONE Year
# DONE Month
# ClientId exists
# Fetch all active events
# Fetch valid workshops for given year and month
# Find the beginning and end dates of the year and month combo
# Flush out the dates of the month with their day i.e. Monday, Tuesday
# Remove blackout dates for a location
# Start Function
# Loop across the dates of the month
# check what day of week date is
# for event that matches that day of week, add it to the Calendar Events list
# End Function
# Remove blackout dates for an event
# Add workshop(s) to the Calendar Events list
# START Table data for CSV
# Add header row to the table
# Go through the calendar events and find the columns needed per row. Make sure this works for Workshops
# END Table data for CSV
# Generate CSV
