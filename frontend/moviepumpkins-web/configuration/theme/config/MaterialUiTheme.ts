import { alpha, createTheme } from "@mui/material";

const theme = createTheme({
  typography: (palette) => ({
    h1: {
      fontSize: "2rem",
      borderLeft: `5px solid ${palette.primary.main}`,
      margin: "0.5rem 0",
      paddingLeft: "1rem",
      fontWeight: "bold",
    },
    h3: {
      fontSize: "1.5rem",
      fontWeight: "bold",
    },
    important: {
      fontWeight: "bold",
      color: palette.error.main,
      letterSpacing: 1,
    },
  }),
  components: {
    MuiAppBar: {
      defaultProps: { variant: "elevation" },
      styleOverrides: {
        root: ({ theme }) => ({
          position: "initial",
          boxShadow: "none",
          background: theme.palette.background.paper,
        }),
      },
    },
    MuiButton: {
      defaultProps: { disableRipple: true },
      variants: [
        {
          props: { variant: "contained" },
          style: ({ theme }) => ({
            boxShadow: "none",
            background: theme.palette.secondary.main,
            color: "white",
            borderRadius: theme.shape.borderRadius,
            "&:hover": {
              boxShadow: "none",
              background: theme.palette.secondary.dark,
            },
          }),
        },
        {
          props: { variant: "text" },
          style: ({ theme }) => ({
            color: theme.palette.secondary.dark,
            borderRadius: theme.shape.borderRadius,
            paddingLeft: theme.spacing(2),
            paddingRight: theme.spacing(2),
            "&:hover": {
              background: theme.palette.secondary.light,
            },
          }),
        },
        {
          props: { color: "warning" },
          style: ({ theme }) => ({
            color: theme.palette.error.main,
            ":hover": {
              color: theme.palette.error.dark,
              backgroundColor: alpha(theme.palette.error.main, 0.1),
            },
          }),
        },
      ],
    },
    MuiIconButton: {
      defaultProps: { disableRipple: true },
      styleOverrides: {
        root: ({ theme }) => ({
          borderRadius: theme.shape.borderRadius,
          ":hover": {
            background: theme.palette.secondary.light,
          },
        }),
      },
    },
    MuiRating: {
      styleOverrides: {
        root: ({ theme }) => ({
          "& span, & span svg": {
            width: "3rem",
            height: "3rem",
          },
        }),
        iconFilled: ({ theme }) => ({
          "& svg": {
            fill: theme.palette.secondary.main,
          },
        }),
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: ({ theme }) => ({
          borderRadius: theme.shape.borderRadius,
        }),
      },
    },
    MuiCard: {
      variants: [
        {
          props: { variant: "outlined" },
          style: ({ theme }) => ({
            borderRadius: "10px",
          }),
        },
      ],
    },
    MuiChip: {
      variants: [
        {
          props: { variant: "accent" },
          style: ({ theme }) => ({
            background: theme.palette.primary.main,
          }),
        },
        {
          props: { color: "gold" },
          style: ({ theme }) => ({
            background: theme.palette.gold.main,
          }),
        },
      ],
    },
    MuiLink: {
      styleOverrides: {
        root: ({ theme }) => ({
          color: theme.palette.info.main,
          textDecoration: "dashed",
          "&:hover": {
            color: theme.palette.info.dark,
          },
        }),
      },
    },
    MuiPagination: {
      defaultProps: { shape: "rounded" },
    },
    MuiPaginationItem: {
      styleOverrides: {
        root: ({ theme }) => ({
          "&.Mui-selected": {
            backgroundColor: theme.palette.primary.dark,
            color: "white",
            ":hover": {
              backgroundColor: theme.palette.primary.dark,
              color: "white",
            },
          },
          "&.MuiPaginationItem-ellipsis:hover": {
            background: "none",
          },
          ":hover": {
            background: "none",
            color: theme.palette.primary.dark,
          },
        }),
      },
    },
    MuiListItemButton: {
      styleOverrides: {
        root: ({ theme }) => ({
          ":hover": {
            color: theme.palette.secondary.dark,
            background: theme.palette.secondary.light,
            "& .MuiListItemIcon-root": {
              color: theme.palette.secondary.dark,
            },
          },
        }),
      },
    },
    MuiMenu: {
      styleOverrides: {
        paper: ({ theme }) => ({
          boxShadow: "none",
          borderWidth: 1,
          borderColor: theme.palette.outline.main,
        }),
        list: {
          padding: 0,
        },
      },
    },
    MuiMenuItem: {
      styleOverrides: {
        root: ({ theme }) => ({
          ":hover": {
            color: theme.palette.secondary.dark,
            background: theme.palette.secondary.light,
            "& .MuiListItemIcon-root": {
              color: theme.palette.secondary.dark,
            },
          },
        }),
      },
      variants: [
        {
          props: { color: "logout" },
          style: ({ theme }) => ({
            color: theme.palette.error.dark,
            ":hover": {
              background: alpha(theme.palette.dislike, 0.1),
              color: theme.palette.error.dark,
              "& .MuiListItemIcon-root": {
                color: theme.palette.error.dark,
              },
            },
            "& .MuiListItemIcon-root": {
              color: theme.palette.error.dark,
            },
          }),
        },
      ],
    },
    MuiSnackbar: {
      variants: [
        {
          props: { color: "success" },
          style: ({ theme }) => ({
            "& .MuiPaper-root": {
              background: theme.palette.success.main,
              boxShadow: "none",
            },
          }),
        },
      ],
    },
  },
  palette: {
    background: {
      default: "#efefef",
      paper: "#fff",
    },
    primary: {
      main: "#ff834a",
      dark: "#e65c32",
      light: "#ffc180",
    },
    secondary: {
      main: "#53a6f3",
      dark: "#3c8ce1",
      light: "#dbe4ff",
    },
    outline: {
      main: "#bdbdbd",
      light: "#dfdfdf",
    },
    gold: {
      main: "#ffb300",
      light: "#ffe9a5",
      dark: "#ea8400",
    },
    like: "#2b7ed7",
    dislike: "#d7332b",
  },
  shape: {
    borderRadius: "5px",
  },
});

export { theme };
