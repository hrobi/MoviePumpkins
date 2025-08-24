"use client";

import { VSpacer } from "@/components/Spacer";
import { Container } from "@mui/material";

interface MainProps {
  children: React.ReactNode;
}

export default function Main({ children }: MainProps) {
  return (
    <>
      <VSpacer size={2} />
      <Container maxWidth="xl">{children}</Container>
    </>
  );
}
