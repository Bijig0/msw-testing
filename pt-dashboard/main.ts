// import * as R from "fp-ts/Record";
// import { pipe } from "fp-ts/lib/function";

// const objToRecord = (record: Record<PropertyKey, any>) => {
//   return pipe(
//     record,
//     R.map((value) => value)
//   );
// };

const myObj = {
  records: [1, 2, 3],
};

// const headers = {
//   headers: ["hereader", "thereader"],
// };

console.log(Object.fromEntries(Object.entries(myObj)));
