import { getUserProtected } from "@/configuration/api-client/clients/pumpkins-core";
import { UserRole } from "@/configuration/user/model";
import { JWT } from "next-auth/jwt";
import { match, P } from "ts-pattern";

export async function fetchUserOrThrow({ token }: { token: JWT }): Promise<{
  username: string;
  displayName: string;
  role: UserRole;
}> {
  const user = await getUserProtected({
    path: { username: token.username! },
  });

  return match(user)
    .with({ data: P.nonNullable }, ({ data }) => data)
    .with({ error: P.nonNullable }, ({ error }) => {
      throw new Error(
        `User couldn't be fetched. Pumpkins backend core gave us the following error: ${JSON.stringify(
          error
        )}`
      );
    })
    .exhaustive();
}
