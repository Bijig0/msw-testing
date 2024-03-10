---
sidebar_position: 2
---

# What are the Excel File States?

There are 5 main types of Excel File States

# Generic Excel File States

1. Excel File

This is the file provided by Supabase

2. Excel Workbook/Worksheet (parsed by exceljs)

This is the `ExcelJS.Workbook` instance provided by `exceljs`

3. Worksheet Arrays

There are `Excel Worksheet` types from `exceljs` being manually parsed as a `Worksheet` through the `getSheetValues` provided by the `exceljs` library. Their typing for what a worksheet is is weird so I decided to make my own.

---

# Application Excel File States

This is the boundary between generic excel worksheet stuff and our application specific actions.

With our `Worksheet Arrays`, we have essentially entered our javascript land. Now we can convert our `Worksheet Arrays` into a service-specific **object schema**, or service-specific **arrays**.

One such example is the **Rekapan** service. From `Worksheet Arrays` we can convert into a `Rekapan Object` or `Rekapan Arrays`.

4. (Service-Specific) Object

This contains specific metadata on the `Worksheet Arrays` tailored to the specific service. e.g. with `Rekapan Object` you have a `prevBulanTotalSewaAlatAmount`, `currentBulanTotalSewaAlatAmount`, `header`, and `records`. This may be different per service and most probably will be.

5. (Service-Specific) Arrays

These are the arrays that will be used by AG-Grid to display to the screen. These are constructed from the (Service-Specific) Object.

# Reasoning for specific states

The reasoning for having a `Worksheet Arrays` state is that, it should just be a generic parser over the entire worksheet. It should be interoperable between excel libraries, the reason I'm using it for exceljs even is because its type system is weird for an `ExcelRow`, so I chose to make my own `Worksheet Arrays` parsing mechanism

The reasoning for needing to convert from a `Rekapan Object` to a `Rekapan Arrays` instead of directly from `Worksheet Arrays` to `Rekapan Arrays` is because `Rekapan Object` will hold an intermediary state that contains all the metadata related to `Rekapan Arrays` which will make it easier to formulate the `Rekapan Arrays` through using what are essentially building blocks.

# Basic Flow

From a basic standpoint, the flow from supabase to display is

`Supabase` -> `Excel File` -> `Excel Workbook/Worksheet` -> `Worksheet Arrays` -> `Service-Specific Object` -> `Service Specific Arrays` (displayed onto the screen)

The reason for these distinctions is due to the naming conventions used for the excel conversion functions
