import { getUserProfile } from "@/configuration/api-client/clients/pumpkins-core";
import { getSessionUser } from "@/configuration/auth/lib";
import { UserProfile } from "@/features/user-profile/model";

export async function fetchUserProfile() {
  const user = await getSessionUser();
  let profile: UserProfile | undefined = undefined;
  if (user) {
    const getUserProfileResponse = await getUserProfile({
      path: { username: user.username },
      next: { tags: ["getUserProfile"] },
    });
    profile = getUserProfileResponse!.data;
  }
  return profile;
}
