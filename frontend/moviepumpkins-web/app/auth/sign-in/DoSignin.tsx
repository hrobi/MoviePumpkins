'use client';

import { signIn } from "next-auth/react";
import { useEffect } from "react";

export default function () {
    useEffect(() => {
        signIn("keycloak").then();
    }, []);
    return <></>;
}