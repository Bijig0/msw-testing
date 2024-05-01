import { Tables } from "app/types/types_db";
import { Products } from "./types";

type Price = Tables<"prices">;

const findCoursePrice = (courseName: string, products: Products): Price[] => {
  const courseprice = products.find((product) => product.name === courseName);
  return courseprice?.prices;
};

export default findCoursePrice;
