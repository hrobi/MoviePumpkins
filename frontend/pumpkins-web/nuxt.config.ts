// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: ["@nuxt/eslint", "@nuxt/ui", "nuxt-auth-utils", "nuxt-open-fetch"],

  devtools: {
    enabled: true,
  },

  css: ["~/assets/css/main.css"],

  runtimeConfig: {
    apiBaseUrl: "",
  },

  routeRules: {
    "/": { prerender: true },
  },

  compatibilityDate: "2025-01-15",

  eslint: {
    config: {
      stylistic: {
        commaDangle: "never",
        braceStyle: "1tbs",
      },
    },
  },

  openFetch: {
    clients: {
      backendCoreApi: {
        baseURL: "http://localhost:8080",
        schema: "../../api/pumpkins-openapi.yaml",
      },
      api: {
        baseURL: "http://localhost:3000/proxy",
        schema: "../../api/pumpkins-openapi.yaml",
      },
    },
  },
});
