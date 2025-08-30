"use client";

import { Box } from "@mui/material";

export function HSpacer({ size }: { size: number | string }) {
  return <Box component="span" sx={{ marginRight: size }}></Box>;
}

export function VSpacer({ size }: { size: number | string }) {
  return <Box sx={{ marginTop: size }}></Box>;
}
