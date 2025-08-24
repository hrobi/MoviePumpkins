"use client";

import { signIn } from "next-auth/react";
import { useEffect } from "react";

export function DoSignin() {
  useEffect(() => {
    signIn("keycloak").then();
  }, []);
  return <></>;
}
