"use client";

import { VSpacer } from "@/features/app/components/Spacer";
import { Container } from "@mui/material";

interface MainProps {
  children: React.ReactNode;
}

export function MainSpace({ children }: MainProps) {
  return (
    <>
      <VSpacer size={2} />
      <Container maxWidth="xl">{children}</Container>
    </>
  );
}
