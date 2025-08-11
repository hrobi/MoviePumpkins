import { authOptions } from "@/app/api/auth/[...nextauth]/route";
import DoSignin from "@/app/auth/signin/DoSignin";
import GoHomeButton from "@/app/auth/signin/GoHomeButton";
import ErrorMessage from "@/components/message-box/ErrorMessage";
import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";

export default async function ({ searchParams }) {
  const session = await getServerSession(authOptions);
  const searchParamsResolved = await searchParams;
  console.log(session);
  if (searchParamsResolved.error) {
    return (
      <>
        <div className="content-section">
          <ErrorMessage title="Fatal Error">
            <div className="flex flex-row">
              {translateError(searchParamsResolved.error)}
              <GoHomeButton />
            </div>
          </ErrorMessage>
        </div>
      </>
    );
  }
  if (searchParamsResolved.callbackUrl && session) {
    throw redirect(decodeURI(searchParamsResolved.callbackUrl)!.toString());
  }

  if (session) {
    throw redirect("/");
  }

  return <DoSignin />;
}

function translateError(error: "Callback" | "OAuthSignin"): string {
  switch (error) {
    case "OAuthSignin":
      return "Authorization server was unreachable!";
    case "Callback":
      return "Communication with the backend server was impossible!";
    default:
      return "Unexpected error occured!";
  }
}
