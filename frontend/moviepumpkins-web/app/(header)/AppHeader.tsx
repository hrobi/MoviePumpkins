import Band from "@/components/Band";
import PrimaryLink from "@/components/PrimaryLink";
import TextInput from "@/components/TextInput";
import { Search } from "lucide-react";

export default function AppHeader() {
  return (
    <>
      <Band tag="header">
        <div className="flex lg:flex-row flex-col gap-3 xl:px-0 px-5 w-full xl:w-2/3 mx-auto py-5 items-stretch">
          <div className="grow relative">
            <TextInput placeholder="Search for title of series or movie" />
            <Search
              className="absolute right-3 top-0 h-full"
              color="var(--outline-color)"
            />
          </div>
          <div className="w-full lg:w-fit">
            <PrimaryLink href="/auth/signin" className="w-full text-center">
              Sign in
            </PrimaryLink>
          </div>
        </div>
      </Band>
    </>
  );
}
