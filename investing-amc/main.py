import csv
file_name = ""

if __name__ == '__main__':
    # read from prices_round_1

    with open('prices_round_1.csv', 'r') as f:
        reader = csv.reader(f)
        next(reader)
        for row in reader:
            print(row)