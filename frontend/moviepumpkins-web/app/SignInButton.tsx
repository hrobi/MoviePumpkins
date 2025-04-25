'use client';

import { signIn } from "next-auth/react";

export default function () {
    return (<>
        <button onClick={_ => signIn('keycloak')}>Sign in!</button>
    </>);
}