---
sidebar_position: 3
---

# What are the Excel Conversion Functions?

These are the functions that take us from one excel state to another.

The reason for having these clearly defined is for naming convention purposes and to explain reasoning behind the conversions

# Naming Convention

With the naming convention of conversions. We start off with `convertExcel...To...` So we `convertExcelWorksheetArraysToRekapanArrays`. We include the `Excel` in the beginning conversion state, but can omit it at the end for brevity.

Also note that there are functions that take you between intermediary states. e.g. `convertExcelWorkbookToRekapanObj`. These functions are useful for when you need the excel files for different purposes. e.g. the `Rekapan Arrays` are mainly used for displaying onto AG-Grid. But the `Rekapan Obj` contains metadata that is crucial for `generateRekapan` as it can provide the metadata on the previous month's `Rekapan`
