"use client";

import {
  Badge,
  Divider,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";

import AccountBoxIcon from "@mui/icons-material/AccountBox";
import ArticleIcon from "@mui/icons-material/Article";
import NotificationsIcon from "@mui/icons-material/Notifications";

interface MenuParams {}

export function MainMenu() {
  return (
    <List>
      <ListItem>
        <ListItemButton>
          <ListItemIcon>
            <Badge badgeContent={4} color="info">
              <NotificationsIcon />
            </Badge>
          </ListItemIcon>
          <ListItemText>Notifications</ListItemText>
        </ListItemButton>
      </ListItem>
      <Divider />
      <ListItem>
        <ListItemButton href="/profile">
          <ListItemIcon>
            <AccountBoxIcon />
          </ListItemIcon>
          <ListItemText>Profile</ListItemText>
        </ListItemButton>
      </ListItem>
      <ListItem>
        <ListItemButton>
          <ListItemIcon>
            <ArticleIcon />
          </ListItemIcon>
          <ListItemText>Watchlist</ListItemText>
        </ListItemButton>
      </ListItem>
    </List>
  );
}
