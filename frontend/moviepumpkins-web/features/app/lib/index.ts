import { nextAuthOptions } from "@/configuration/auth";
import { getServerSession, Session } from "next-auth";

export async function getUser(): Promise<Session["user"]> {
    const session = await getServerSession(nextAuthOptions);
    return session?.user;
}

export async function isAuthenticated(): Promise<boolean> {
    const session = await getServerSession(nextAuthOptions);
    return session == null;
}

export const unexpectedNoAuthError = (status: number): Error => {
    return new Error(
        `Backend Core needs ${
            status == 401 ? "authentication" : "authorization"
        } for this action, however application shouldn't require it at this point, something went wrong!`,
    );
};
