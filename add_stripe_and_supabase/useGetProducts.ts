import { useQuery } from "@tanstack/react-query";

import { Products, productsQuery } from "./mystripe/types";

const useGetProducts = () => {
  const getSoleTraderProducts = async (): Promise<Products> => {
    const { data: products, error } = await productsQuery;

    if (error) throw error;

    return products;
  };

  const result = useQuery({
    queryFn: getSoleTraderProducts,
    queryKey: ["soleTraderProducts"],
  });
  return result;
};

export default useGetProducts;
