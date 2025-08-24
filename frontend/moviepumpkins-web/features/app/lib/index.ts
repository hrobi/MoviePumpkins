import { nextAuthOptions } from "@/configuration/auth";
import { getServerSession, Session } from "next-auth";

export async function getUser(): Promise<Session["user"]> {
  const session = await getServerSession(nextAuthOptions);
  return session?.user;
}
export async function withUser<T>(
  produce: (
    user: NonNullable<Session["user"]>,
    authParams: () => { header: { Authorization: string } }
  ) => T
): Promise<T> {
  const user = (await getUser())!;
  return produce(user, () => ({
    header: {
      Authorization: `bearer ${user!.accessToken}`,
    },
  }));
}
