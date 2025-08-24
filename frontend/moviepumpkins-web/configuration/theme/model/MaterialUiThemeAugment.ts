declare module "@mui/material/styles" {
  // allow configuration using `createTheme()`
  interface PaletteOptions {
    outline: {
      main: string;
      light: string;
    };
    gold: {
      main: string;
      light: string;
      dark: string;
    };
    like: string;
    dislike: string;
  }
  interface Palette {
    outline: {
      main: string;
      light: string;
    };
    gold: {
      main: string;
      dark: string;
      light: string;
    };
    like: string;
    dislike: string;
  }
}

declare module "@mui/material/Chip" {
  interface ChipPropsVariantOverrides {
    accent: true;
  }
  export interface ChipPropsColorOverrides {
    gold: true;
  }
}

declare module "@mui/material/Typography" {
  interface TypographyPropsVariantOverrides {
    important: true;
  }
}
