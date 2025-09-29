import { defineConfig } from "@hey-api/openapi-ts";

export default defineConfig({
    input: "./.core-apispec.yml",
    output: "./_generated/configuration/api-client/clients/pumpkins-core",
    plugins: [
        {
            name: "@hey-api/client-next",
            runtimeConfigPath: "./PumpkinsClientConfig.ts",
        },
    ],
});
