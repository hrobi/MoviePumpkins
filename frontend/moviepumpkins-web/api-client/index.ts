import createClient from "openapi-fetch";
import type { paths } from "./core-client.d";

export const coreClient = createClient<paths>({
    baseUrl: process.env.BACKEND_CORE_URL_BASE!,
});