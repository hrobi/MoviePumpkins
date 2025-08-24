import { paths } from "@/configuration/api-client/model";
import createClient from "openapi-fetch";

export const pumpkinsCoreClient = createClient<paths>({
  baseUrl: process.env.BACKEND_CORE_URL_BASE!,
});
