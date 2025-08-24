import { coreClient } from "@/api-client";
import Section from "@/components/Section";
import { UserProfile } from "@/model/User";
import { getServerSession } from "next-auth";
import { UserProfileMenu } from "../(scaffold)/UserProfileMenu";
import { authOptions } from "../api/auth/[...nextauth]/route";

async function fetchProfile(): Promise<UserProfile> {
  const session = await getServerSession(authOptions);
  const user = session!.user!;
  const profileOrError = await coreClient.GET("/users/{username}/profile", {
    params: { path: { username: user.username } },
  });

  return profileOrError.data!;
}

export default async function Page() {
  const profile = await fetchProfile();

  return (
    <>
      <Section title="Profile">
        <UserProfileMenu profile={profile} />
      </Section>
    </>
  );
}
