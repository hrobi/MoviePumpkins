import { coreClient } from "@/api-client";
import { UserProfile } from "@/model/User";
import { getServerSession } from "next-auth";
import { authOptions } from "../api/auth/[...nextauth]/route";
import AppHeader from "./AppHeader";

export default async function Scaffold() {
  const session = await getServerSession(authOptions);
  let profile: UserProfile | undefined = undefined;
  if (session?.user) {
    const getUserProfileResponse = await coreClient.GET(
      "/users/{username}/profile",
      { params: { path: { username: session.user.username } } }
    );
    profile = getUserProfileResponse!.data;
  }

  return (
    <>
      <AppHeader user={session?.user} profile={profile} />
    </>
  );
}
