---
sidebar_position: 2
---

# Introduction

How the excel zod fp-ts shit works or should work

## Getting Started

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
