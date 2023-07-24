import calendar
from datetime import date


def get_date_first_of_month(year: int, month: int):
    return date(year, month, 1)


def get_date_last_of_month(year: int, month: int):
    dates_of_month_range = calendar.monthrange(year, month)
    return date(year, month, dates_of_month_range[1])
