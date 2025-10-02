import { CreateClientConfig } from "@/configuration/api-client/clients/pumpkins-core/client";

export const createClientConfig: CreateClientConfig = (config) => ({
  ...config,
  baseUrl: process.env.BACKEND_CORE_URL_BASE,
});
