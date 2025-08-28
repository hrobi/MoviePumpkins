"use client";

import { Alert, AlertTitle, Typography } from "@mui/material";

interface MediaNotFoundAlertParams {
  id: number;
}

export function MediaNotFoundAlert({ id }: MediaNotFoundAlertParams) {
  return (
    <Alert severity="error" variant="filled">
      <AlertTitle>Oops media not found!</AlertTitle>
      <Typography variant="body1">
        The media you were looking for with id `{id}` appears not to exist!
      </Typography>
    </Alert>
  );
}
