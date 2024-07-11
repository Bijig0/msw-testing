import { render } from "@testing-library/react";
import { HttpResponse, http } from "msw";
import { setupServer } from "msw/node";
import App from "./App";

const url = "https://YOUR_URL_HERE.supabase.co/rest/v1/todos";

const handlers = [
  http.get(url, () => {
    return HttpResponse.json("Working");
  }),
];

export const server = setupServer(...handlers);

describe("supabase test", () => {
  it("should replace the data", () => {
    server.listen();
    server.events.on("request:start", (req) => {
      console.log(`Outgoing: ${req}`);
    });
    fetch(url);
    render(<App />);
  });
});
