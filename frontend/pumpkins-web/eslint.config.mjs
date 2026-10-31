// @ts-check
import withNuxt from "./.nuxt/eslint.config.mjs";

export default withNuxt({
  rules: {
    "vue/no-multiple-template-root": "off",
    "vue/comma-dangle": "off",
    "vue/valid-template-root": "off",
    "@typescript-eslint/no-unused-vars": "warn",
    "@typescript-eslint/no-explicit-any": "warn",
    "vue/singleline-html-element-content-newline": "off",
    "@stylistic/quotes": ["error", "double"],
    "@stylistic/semi": ["error", "always"],
    "@stylistic/member-delimiter-style": [
      "error",
      {
        multiline: { delimiter: "semi", requireLast: true },
        singleline: { delimiter: "semi", requireLast: true },
      },
    ],
    "@stylistic/comma-dangle": ["error", "always-multiline"],
  },
});
