import Band from "@/components/Band";
import PrimaryLink from "@/components/PrimaryLink";
import SecondaryLink from "@/components/SecondaryLink";
import SecondaryLinkRed from "@/components/SecondaryLinkRed";
import TextInput from "@/components/TextInput";
import TextWithIcon from "@/components/TextWithIcon";
import {
  GripIcon,
  ListCheckIcon,
  Search,
  UserRoundPenIcon,
} from "lucide-react";
import { getServerSession } from "next-auth";
import { match, P } from "ts-pattern";
import { authOptions } from "../api/auth/[...nextauth]/route";

export default async function AppHeader() {
  const session = await getServerSession(authOptions);
  const links = match(session?.user)
    .with(P.nullish, () => (
      <>
        <SecondaryLink href="">
          <TextWithIcon text="Watchlist" icon={<ListCheckIcon />} />
        </SecondaryLink>
        <PrimaryLink href="/auth/signin" className="text-center">
          Sign in
        </PrimaryLink>
      </>
    ))
    .with({ role: "reviewer" }, () => (
      <>
        <SecondaryLink href="">
          <TextWithIcon text="Watchlist" icon={<ListCheckIcon />} />
        </SecondaryLink>
        <SecondaryLink href="/profile">
          <TextWithIcon text="Profile" icon={<UserRoundPenIcon />} />
        </SecondaryLink>
        <SecondaryLink href="">
          <TextWithIcon text="Other" icon={<GripIcon />} />
        </SecondaryLink>
      </>
    ))
    .with({ role: P.union("admin", "supervisor") }, () => (
      <>
        <SecondaryLink href="">
          <TextWithIcon text="Watchlist" icon={<ListCheckIcon />} />
        </SecondaryLink>
        <SecondaryLink href="/alerts">
          <TextWithIcon text="Alerts" icon={<UserRoundPenIcon />} />
        </SecondaryLink>
        <SecondaryLink href="">
          <TextWithIcon text="Other" icon={<GripIcon />} />
        </SecondaryLink>
      </>
    ))
    .exhaustive();

  return (
    <>
      <Band tag="header">
        <div className="flex lg:flex-row flex-col gap-3 xl:px-0 px-5 w-full xl:w-2/3 mx-auto items-center">
          <div className="grow relative w-full">
            <TextInput placeholder="Search for title of series or movie" />
            <Search
              className="absolute right-3 top-0 h-full"
              color="var(--outline-color)"
            />
          </div>
          <div className="flex-none w-full lg:w-fit py-5 flex flex-row gap-2 justify-center items-center">
            {links}
            {session && (
              <SecondaryLinkRed href="/auth/signout">Sign out</SecondaryLinkRed>
            )}
          </div>
        </div>
      </Band>
    </>
  );
}
