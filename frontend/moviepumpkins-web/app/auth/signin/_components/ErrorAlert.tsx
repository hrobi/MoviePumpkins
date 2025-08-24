"use client";

import { Alert, AlertTitle } from "@mui/material";

export function ErrorAlert({
  explanation,
  title,
}: {
  title: string;
  explanation: string;
}) {
  return (
    <Alert variant="filled" severity="error">
      <AlertTitle>{title}</AlertTitle>
      {explanation}
    </Alert>
  );
}
