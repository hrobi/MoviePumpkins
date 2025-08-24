"use client";

import {
  AppBar,
  Button,
  Container,
  Divider,
  Drawer,
  IconButton,
  InputBase,
  Stack,
  styled,
  Toolbar,
} from "@mui/material";

import { UserProfileMenu } from "@/app/(scaffold)/UserProfileMenu";
import { useMenuAnchor } from "@/hooks";
import { UserProfile } from "@/model/User";
import AppsIcon from "@mui/icons-material/Apps";
import ArticleIcon from "@mui/icons-material/Article";
import PersonIcon from "@mui/icons-material/Person";
import SearchIcon from "@mui/icons-material/Search";
import { Session } from "next-auth";
import { useState } from "react";
import { MainMenu } from "./MainMenu";

const Search = styled("div")(({ theme }) => ({
  position: "relative",
  borderRadius: theme.shape.borderRadius,
}));

const SearchIconWrapper = styled("div")(({ theme }) => ({
  position: "absolute",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  height: "100%",
  paddingTop: theme.spacing(0.5),
  paddingBottom: theme.spacing(0.5),
  paddingLeft: theme.spacing(1.5),
  paddingRight: theme.spacing(1.5),
  right: 0,
}));

const SearchInput = styled(InputBase)(({ theme }) => ({
  paddingTop: theme.spacing(0.5),
  paddingBottom: theme.spacing(0.5),
  paddingLeft: theme.spacing(1.5),
  paddingRight: theme.spacing(1.5),
  borderWidth: 1,
  borderStyle: "solid",
  borderColor: theme.palette.outline.main,
  borderRadius: theme.shape.borderRadius,
  width: "100%",
  "&.Mui-focused": {
    borderColor: theme.palette.primary.light,
    borderWidth: 2,
    paddingTop: `calc(${theme.spacing(0.5)} - 1px)`,
    paddingBottom: `calc(${theme.spacing(0.5)} - 1px)`,
    paddingLeft: `calc(${theme.spacing(1.5)} - 1px)`,
    paddingRight: `calc(${theme.spacing(1.5)} - 1px)`,
  },
}));

interface AppHeaderParams {
  user?: Session["user"];
  profile?: UserProfile;
}

export default function AppHeader({ user, profile }: AppHeaderParams) {
  const [mainMenuOpen, setMainMenuOpen] = useState(false);
  const {
    anchorEl: anchorElement,
    open,
    onClose,
    openOnEvent,
  } = useMenuAnchor();

  return (
    <>
      <AppBar>
        <Toolbar>
          <Container maxWidth="xl">
            <Stack direction="row" spacing={1}>
              <Search sx={{ flexGrow: 1 }}>
                <SearchIconWrapper>
                  <SearchIcon />
                </SearchIconWrapper>
                <SearchInput placeholder="Search media" />
              </Search>
              <Stack
                direction="row"
                sx={{ display: { xs: "none", md: "flex" } }}
                spacing={1}
              >
                <Button
                  variant="text"
                  startIcon={<ArticleIcon />}
                  href="/watchlist"
                >
                  Watchlist
                </Button>
                {!user && (
                  <Button variant="contained" href="/auth/signin">
                    Login
                  </Button>
                )}
                <Divider flexItem orientation="vertical" />
              </Stack>
              <IconButton onClick={() => setMainMenuOpen(true)}>
                <AppsIcon color="secondary" />
              </IconButton>
            </Stack>
          </Container>
          {user && (
            <>
              <IconButton onClick={openOnEvent}>
                <PersonIcon />
              </IconButton>
              <UserProfileMenu
                open={open}
                anchorEl={anchorElement}
                onClose={onClose}
                profile={profile!}
              />
            </>
          )}
        </Toolbar>
      </AppBar>
      <Drawer open={mainMenuOpen} onClose={() => setMainMenuOpen(false)}>
        <MainMenu />
      </Drawer>
    </>
  );
}
