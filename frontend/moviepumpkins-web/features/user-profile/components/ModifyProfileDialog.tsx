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
        data: profile,
        state: {status: "uninitialized"},
    });

    const [updateJustSucceeded, setUpdateJustSucceeded] = useState(false);
    useEffect(() => {
        setUpdateJustSucceeded(!isPending && state.state.status === "ok");
    }, [isPending]);

    const getFieldError = (name: string) =>
        state.state.status === "requestBodyError"
            ? state.state.errors
                .filter(({fields}) => fields.includes(name))
                .map(({reason}) => reason)
            : [];

    const inputErrors = {
        displayName: getFieldError("displayName"),
        fullName: getFieldError("fullName"),
        email: getFieldError("email"),
    };

    return (
        <Dialog
            open={open}
            onClose={() => onClose()}
            fullWidth
            sx={{position: "relative"}}
        >
            <DialogTitle>Modify your profile</DialogTitle>
            <Snackbar
                open={updateJustSucceeded}
                onClose={() => setUpdateJustSucceeded(false)}
                message="Profile updated successfully"
                anchorOrigin={{vertical: "top", horizontal: "center"}}
                color="success"
            />
            <form action={formAction}>
                <List>
                    <ListItem>
                        <TextField
                            label="Display name"
                            defaultValue={state.data.displayName}
                            error={inputErrors.displayName.length > 0}
                            helperText={inputErrors.displayName[0]}
                            name="displayName"
                            disabled={isPending}
                            fullWidth
                        />
                    </ListItem>
                    <ListItem>
                        <TextField
                            label="Full name"
                            defaultValue={state.data.fullName}
                            error={inputErrors.fullName.length > 0}
                            helperText={inputErrors.fullName[0]}
                            name="fullName"
                            disabled={isPending}
                            fullWidth
                        />
                    </ListItem>
                    <ListItem>
                        <TextField
                            label="E-mail"
                            defaultValue={state.data.email}
                            error={inputErrors.email.length > 0}
                            helperText={inputErrors.email[0]}
                            name="email"
                            disabled={isPending}
                            fullWidth
                        />
                    </ListItem>
                    <ListItem sx={{justifyContent: "end"}}>
                        <Button variant="contained" type="submit" disabled={isPending}>
                            Modify
                        </Button>
                    </ListItem>
                </List>
            </form>
            <Backdrop open={isPending}>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    );
}
