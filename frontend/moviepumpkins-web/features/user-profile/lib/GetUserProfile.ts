import { pumpkinsCoreClient } from "@/configuration/api-client";
import { getSessionUser } from "@/configuration/auth/lib";
import { UserProfile } from "@/features/user-profile/model";

export async function getUserProfile() {
  const user = await getSessionUser();
  let profile: UserProfile | undefined = undefined;
  if (user) {
    const getUserProfileResponse = await pumpkinsCoreClient.GET(
      "/users/{username}/profile",
      { params: { path: { username: user.username } } }
    );
    profile = getUserProfileResponse!.data;
  }
  return profile;
}
