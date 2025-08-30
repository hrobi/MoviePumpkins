import { getSessionUser } from "@/configuration/auth/lib";
import { AppHeader } from "@/features/app.scaffold/components";
import { fetchUserProfile } from "@/features/user-profile/lib";

export async function Scaffold() {
  const user = await getSessionUser();
  const profile = await fetchUserProfile();

  return (
    <>
      <AppHeader user={user} profile={profile} />
    </>
  );
}
