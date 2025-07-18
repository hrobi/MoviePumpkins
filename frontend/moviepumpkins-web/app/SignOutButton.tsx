'use client'

import { signOut } from "next-auth/react";

export default function () {
    return (<>
        <button onClick={_ => signOut()}>Sign out!</button>
    </>);
}