import SignInButton from "@/app/SignInButton";
import SignOutButton from "@/app/SignOutButton";
import { getServerSession } from "next-auth";
import { authOptions } from "./api/auth/[...nextauth]/route";

export default async function () {
    const session = await getServerSession(authOptions);
    return (
        <div>
            <SignInButton/>
            <SignOutButton/>
            <br/>
            session: {JSON.stringify(session)}
            <br/>
            role: {session?.user?.role}
        </div>
    );
}
