import { createClient } from "@/utility/supabase/client";
import { QueryData } from "@supabase/supabase-js";

export const productsQuery = createClient()
  .from("products")
  .select("*, prices(*)")
  .eq("active", true)
  .eq("prices.active", true)
  .order("metadata->index")
  .order("unit_amount", { referencedTable: "prices" });

export type Products = QueryData<typeof productsQuery>;
