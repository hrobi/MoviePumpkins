"use client";

import { Paper, Stack, Typography, useTheme } from "@mui/material";
import React from "react";

interface SectionParams {
  title?: React.ReactNode;
  additionalButton?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
}

export function Section({
  children,
  title,
  className = "",
  additionalButton,
}: SectionParams) {
  const theme = useTheme();

  return (
    <>
      <Paper sx={{ padding: theme.spacing(3), marginTop: theme.spacing(5) }}>
        <Stack direction="row" spacing={1} marginBottom={2}>
          <Typography variant="h1">{title}</Typography>
          {additionalButton}
        </Stack>
        {children}
      </Paper>
    </>
  );
}
