import logging

from datetime import date

logger = logging.getLogger()


def validate_year(year):
    is_valid_year_type: bool = (isinstance(year, int))
    logger.info(is_valid_year_type)

    if not is_valid_year_type:
        logger.error("Input year is not valid: %s", year)
        exit()

    minimum_year_range = 2020
    maximum_year_range = date.today().year + 5

    is_valid_year: bool = minimum_year_range < year < maximum_year_range

    if not is_valid_year:
        logger.error(
            "Input year is not within correct year range %s-%s: %s",
            minimum_year_range, maximum_year_range, year)
        exit()


def validate_month(month):
    is_valid_month_type: bool = (isinstance(month, int))
    logger.info(is_valid_month_type)

    if not is_valid_month_type:
        logger.error("Input month is not valid: %s", month)
        exit()

    minimum_month_range = 1
    maximum_month_range = 12

    is_valid_month: bool = minimum_month_range <= month <= maximum_month_range

    if not is_valid_month:
        logger.error(
            "Input month is not within correct month range %s-%s: %s",
            minimum_month_range, maximum_month_range, month)
        exit()
