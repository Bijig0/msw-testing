---
sidebar_position: 3
---

# What are the Excel Conversion Functions?

These are the functions that take us from one excel state to another.

The reason for having these clearly defined is for naming convention purposes and to explain reasoning behind the conversions

# Service specific excel conversion function

So sometimes you'll find a function like `convertExcelFileToRekapanArrays`. The way this works is we reach the `Worksheet Arrays` state. and from here. ALWAYS. We convert first from `Rekapan Object` then to `Rekapan Arrays` this ensures proper validation is done onto the arrays before they get displayed. (This is the internal workings). BTW It is unsafe to use `Worksheet Arrays` directly to display via AG-Grid but do so if you're feeling lazy. The proper way is to parse to `Rekapan Object` then `Rekapan Arrays` tho.

# Naming Convention

With the naming convention of conversions. We start off with `convertExcel...To...` So we `convertExcelWorksheetArraysToRekapanArrays`. We include the `Excel` in the beginning conversion state, but can omit it at the end for brevity.

Also note that there are functions that take you between intermediary states. e.g. `convertExcelWorkbookToRekapanObj`. These functions are useful for when you need the excel files for different purposes. e.g. the `Rekapan Arrays` are mainly used for displaying onto AG-Grid. But the `Rekapan Obj` contains metadata that is crucial for `generateRekapan` as it can provide the metadata on the previous month's `Rekapan`
