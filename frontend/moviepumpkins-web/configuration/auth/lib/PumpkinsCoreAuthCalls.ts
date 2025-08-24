import { pumpkinsCoreClient } from "@/configuration/api-client";
import { JWT } from "next-auth/jwt";

export async function fetchUserOrThrow({ token }: { token: JWT }) {
  const user = await pumpkinsCoreClient.GET("/users/{username}/protected", {
    params: {
      path: { username: token.username! },
      header: { Authorization: `Bearer ${token.access_token}` },
    },
    cache: "no-cache",
  });
  if (user.error) {
    throw new Error(
      `Communication with the core server was unsuccessful, status code: ${user.response.status}`
    );
  }
  return user.data;
}
