import array
import csv


def create_csv(file_name: str, header_row: array, table_data: array):
    with open(file_name, 'w') as f:
        # using csv.writer method from CSV package
        write = csv.writer(f)

        write.writerow(header_row)
        write.writerows(table_data)
