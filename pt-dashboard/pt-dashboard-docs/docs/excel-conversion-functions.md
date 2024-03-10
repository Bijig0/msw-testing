---
sidebar_position: 1
---

# Excel Conversion Functions

What functions are available

# Excel Conversion Functions

We recover an excel file from supabase.

We then call

`convertExcelFileToArrays`

The naming convention here is extremely important.

Supabase provides us with a file. We can start this off as our `Excel File`.

With our excel parser library. We make sure the `Excel File` is valid, and then call it our `Excel Workbook`.

Within an `Excel Workbook`, we have `Excel Worksheets`

Our `Excel Worksheet` is just an `exceljs` instance. We don't have the actual values. We run it through our zod parser, and then call it our `Worksheet Arrays`.

With our `Worksheet Arrays`, we have essentially entered our javascript land. Now we can convert our `Worksheet Arrays` into a service-specific **object schema**, or service-specific **arrays**.

One such example is the **Rekapan** service. From `Worksheet Arrays` we can convert into a `Rekapan Object` or `Rekapan Arrays`.

Our flow for displaying the excel file should be

1. Retrieve Supabase `Excel File`.
2. Use `exceljs` to convert it into an `Excel Workbook`
3. Convert the `Excel Workbook` into `Rekapan Arrays`.

Service-specific arrays are always how the excel files will end up being displayed because that's just how AG-Grid works.

The function for converting from `Excel Workbook` to `Rekapan Arrays` should be `convertExcelWorkbookToRekapanArrays`.

Within the function however, there should be this distinct flow.

1. Parse an `Excel Worksheet` into `Worksheet Arrays`. This enters javascript land. There should be a function called `convertExcelWorksheetToWorksheetArrays`.

2. Convert `Worksheet Arrays` to a `Rekapan Object` (or service of choice)

3. Convert `Rekapan Object` (or service of choice) to `Rekapan Arrays` (or service of choice)

The reasoning for having a `Worksheet Arrays` state is that, it should just be a generic parser over the entire worksheet. It should be interoperable between excel libraries, the reason I'm using it for exceljs even is because its type system is weird for an `ExcelRow`, so I chose to make my own `Worksheet Arrays` parsing mechanism

The reasoning for needing to convert from a `Rekapan Object` to a `Rekapan Arrays` instead of directly from `Worksheet Arrays` to `Rekapan Arrays` is because `Rekapan Object` will hold an intermediary state that contains all the metadata related to `Rekapan Arrays` which will make it easier to formulate the `Rekapan Arrays` through using what are essentially building blocks.

With the naming convention of conversions. We start off with `convertExcel...To...` So we `convertExcelWorksheetArraysToRekapanArrays`. We include the `Excel` in the beginning conversion state, but can omit it at the end for brevity.

Also note that there are functions that take you between intermediary states. e.g. `convertExcelWorkbookToRekapanObj`. These functions are useful for when you need the excel files for different purposes. e.g. the `Rekapan Arrays` are mainly used for displaying onto AG-Grid. But the `Rekapan Obj` contains metadata that is crucial for `generateRekapan` as it can provide the metadata on the previous month's `Rekapan` 








1. For the first one

-So we have a function convert Excel File to Arrays

So with my excel types,

A worksheet is often modelled as a [][].

This is symbolic of a 2d matrix, or in this case an excel file.

The first [] is the actual record/row, the second [] indicates the list of these records (which ends up making up the worksheet)

So (string | number | date)[][], means a worksheet with records of type (string | number | cell), (a record here just refers to a single cell in the table)

so

```ts
records {
    "2024-05-06": [1,2,3]
}
```

can also mean a record for "2024-05-06", and that specific record contains the values [1,2,3]
now the thing here to take note of is that I think even if youre using array notation (an array of objects), or a group by object (key value), each sort of value/each array represents a single record

Ok Also, for worksheet parsing it's quite simple

Supabase excel file --> parse as generic worksheet --> parse into obj --> parse into specific type (tagihan/rekapan etc.)

this way you get full typing support when you parse as a worksheet first, and are guaranteed of the type, you aprse into Obj to separate headers and records

Ok so the types for a Workbook, is

```ts
CellType[][][]
```

The outermost array means, a workbook contains worksheets

```ts
CellType[][]
```

The second outermost array means a worksheet contains records/rows

```ts
CellType[]
```

The last outermost array (innermost array) means a row contains cells

```ts
CellType;
```
