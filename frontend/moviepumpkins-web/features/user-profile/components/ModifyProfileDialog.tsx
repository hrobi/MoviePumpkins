import { updateProfileAction } from "@/features/user-profile/actions";
import { UserProfile } from "@/features/user-profile/model";
import {
  Backdrop,
  Button,
  CircularProgress,
  Dialog,
  DialogTitle,
  List,
  ListItem,
  Snackbar,
  TextField,
} from "@mui/material";
import { useActionState, useEffect, useState } from "react";

interface ModifyProfileDialogParams {
  open: boolean;
  profile: UserProfile;
  onClose: () => void;
}

export function ModifyProfileDialog({
  open,
  profile,
  onClose,
}: ModifyProfileDialogParams) {
  const [state, formAction, isPending] = useActionState(updateProfileAction, {
    error: undefined,
    data: profile,
  });

  const getFieldError = (
    field: keyof NonNullable<typeof state.error>["fieldErrors"]
  ) => state.error?.fieldErrors[field];

  const [updateJustSucceeded, setUpdateJustSucceeded] = useState(false);
  useEffect(() => {
    setUpdateJustSucceeded(!isPending && !!state.updateSucceeded);
  }, [isPending]);

  return (
    <Dialog
      open={open}
      onClose={() => onClose()}
      fullWidth
      sx={{ position: "relative" }}
    >
      <DialogTitle>Modify your profile</DialogTitle>
      <Snackbar
        open={updateJustSucceeded}
        onClose={() => setUpdateJustSucceeded(false)}
        message="Profile updated successfully"
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
        color="success"
      />
      <form action={formAction}>
        <List>
          <ListItem>
            <TextField
              label="Display name"
              defaultValue={state.data.displayName}
              error={!!getFieldError("displayName")}
              helperText={getFieldError("displayName")}
              name="displayName"
              disabled={isPending}
              fullWidth
            />
          </ListItem>
          <ListItem>
            <TextField
              label="Full name"
              defaultValue={state.data.fullName}
              error={!!getFieldError("fullName")}
              helperText={getFieldError("fullName")}
              name="fullName"
              disabled={isPending}
              fullWidth
            />
          </ListItem>
          <ListItem>
            <TextField
              label="E-mail"
              defaultValue={state.data.email}
              error={!!getFieldError("email")}
              helperText={getFieldError("email")}
              name="email"
              disabled={isPending}
              fullWidth
            />
          </ListItem>
          <ListItem sx={{ justifyContent: "end" }}>
            <Button variant="contained" type="submit" disabled={isPending}>
              Modify
            </Button>
          </ListItem>
        </List>
      </form>
      <Backdrop open={isPending}>
        <CircularProgress />
      </Backdrop>
    </Dialog>
  );
}
