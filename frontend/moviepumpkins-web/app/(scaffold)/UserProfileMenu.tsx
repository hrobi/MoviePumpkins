"use client";

import { UserProfile } from "@/model/User";
import EditIcon from "@mui/icons-material/Edit";
import LogoutIcon from "@mui/icons-material/Logout";
import PersonIcon from "@mui/icons-material/Person";
import {
  Avatar,
  Divider,
  ListItem,
  ListItemIcon,
  ListItemText,
  Menu,
  MenuItem,
  MenuList,
  Stack,
  Typography,
} from "@mui/material";
import Link from "next/link";
import { useState } from "react";
import { ModifyProfileDialog } from "./ModifyProfileDialog";

interface UserProfileCardParams {
  profile: UserProfile;
  anchorEl: HTMLElement | null;
  open: boolean;
  onClose: () => void;
}

export function UserProfileMenu({
  profile,
  anchorEl,
  open,
  onClose,
}: UserProfileCardParams) {
  const [userDialogOpen, setUserDialogOpen] = useState(false);
  return (
    <>
      <Menu open={open} anchorEl={anchorEl} onClose={onClose}>
        <MenuList>
          <ListItem>
            <Stack direction="row" spacing={2} alignItems="center">
              <Avatar>
                <PersonIcon />
              </Avatar>
              <Stack>
                <Typography variant="h3">{profile.displayName}</Typography>
                <Typography variant="body1">@{profile.username}</Typography>
                <Typography variant="important">{profile.role}</Typography>
              </Stack>
            </Stack>
          </ListItem>
          <ListItem>
            <ListItemText primary="Full name" secondary={profile.fullName} />
          </ListItem>
          <ListItem>
            <ListItemText primary="E-mail" secondary={profile.email} />
          </ListItem>
          <Divider />
          <MenuItem onClick={() => setUserDialogOpen(true)}>
            <ListItemIcon>
              <EditIcon />
            </ListItemIcon>
            <ListItemText primary="Modify" />
          </MenuItem>
          <MenuItem color="logout">
            <ListItemIcon>
              <LogoutIcon />
            </ListItemIcon>
            <Link href="/auth/signout">
              <ListItemText primary="Logout" />
            </Link>
          </MenuItem>
        </MenuList>
      </Menu>
      <ModifyProfileDialog
        onClose={() => setUserDialogOpen(false)}
        open={userDialogOpen}
        profile={profile}
      />
    </>
  );
}
