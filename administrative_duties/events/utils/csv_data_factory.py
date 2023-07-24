import array
import logging

logger = logging.getLogger()


class CsvDataFactory:

    @staticmethod
    def csv_row_generator(data_object, data_location: array):
        split_data_location = data_location.split(".")
        temp_data = None

        if len(split_data_location) == 1:
            temp_data = data_object[split_data_location[0]]

        if len(split_data_location) == 2:
            temp_data = data_object[split_data_location[0]][split_data_location[1]]

        return temp_data
