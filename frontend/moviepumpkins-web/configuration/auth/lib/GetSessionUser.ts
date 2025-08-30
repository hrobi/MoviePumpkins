import { nextAuthOptions } from "@/configuration/auth";
import { SessionUser } from "@/configuration/auth/model";
import { getServerSession } from "next-auth";

export async function getSessionUser(): Promise<SessionUser | undefined> {
  return (await getServerSession(nextAuthOptions))?.user;
}
