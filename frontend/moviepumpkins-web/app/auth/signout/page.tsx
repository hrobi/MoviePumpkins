"use client";

import { signOut } from "next-auth/react";
import { useEffect } from "react";

export default function () {
  useEffect(() => {
    signOut({ callbackUrl: "/" }).then();
  }, []);
  return <></>;
}
