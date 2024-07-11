import { createClient } from "@supabase/supabase-js";

const SUPABASE_URL = () => {
  throw new Error("Put in supabase url");
};
const ANON_KEY = () => {
  throw new Error("Put in anon key");
};

export const supabase = createClient(SUPABASE_URL(), ANON_KEY(), {
  global: { fetch: fetch.bind(globalThis) },
});
