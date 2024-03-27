import csv

filename = './boilerplate/src/alat_files/UHEAD JACK 60.csv'

# Open the file in read mode
with open(filename, 'r', newline='') as file:
    # Create a CSV reader object
    csv_reader = csv.reader(file)
    
    # Iterate over each row in the CSV file
    for row in csv_reader:
        # Access each column by its header name

        if (len(row) == 0): continue

        if row[-1] != '0' and row[-2] != '0':
            print(row)
            break