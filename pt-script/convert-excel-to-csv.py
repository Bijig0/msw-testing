from typing import final
import pandas as pd

excel_file = 'STOK LINA 2024 (1).xlsx'

xl = pd.ExcelFile(excel_file)

# This takes an excel spreadsheet with no headers
# THe STOK Alat, and converts it into csv files comma delimited

# Iterate through each sheet in the Excel file
for sheet_name in xl.sheet_names:
    # Read the sheet into a DataFrame to determine the number of columns
    temp_df = xl.parse(sheet_name)
    num_cols = temp_df.shape[1]

    # Ensure we only read up to the first four columns
    cols_to_read = min(num_cols, 4)

    # Read the sheet into a DataFrame, taking only the required columns
    df = xl.parse(sheet_name, usecols=range(cols_to_read), header=None)

    # If the DataFrame has fewer than 4 columns, add missing columns filled with 0
    for i in range(4 - df.shape[1]):
        df[f'Column_{df.shape[1] + i}'] = 0

    # Fill all NaN values in the DataFrame with 0
    df.fillna(0, inplace=True)

    # Convert the DataFrame to a CSV file, comma delimited
    csv_file = f'{sheet_name}.csv'
    df.to_csv(csv_file, index=False, header=False)

    print(f'Converted {sheet_name} to {csv_file}')

