import { defineConfig } from "@hey-api/openapi-ts";

export default defineConfig({
    input: "./server-api.yml",
    output: "./_generated/configuration/api-client/clients/pumpkins-core",
    plugins: [
        {
            name: "@hey-api/client-next",
            runtimeConfigPath: "./PumpkinsClientConfig.ts",
        },
    ],
});
