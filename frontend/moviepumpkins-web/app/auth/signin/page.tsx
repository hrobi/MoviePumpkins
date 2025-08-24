import { authOptions } from "@/app/api/auth/[...nextauth]/route";
import DoSignin from "@/app/auth/signin/_components/DoSignin";
import { getServerSession } from "next-auth";
import { redirect } from "next/navigation";
import { ErrorAlert } from "./_components/ErrorAlert";

interface ErrorMessage {
  title: string;
  explanation: string;
}

export default async function ({ searchParams }) {
  const session = await getServerSession(authOptions);
  const searchParamsResolved = await searchParams;
  console.log(session);
  if (searchParamsResolved.error) {
    const error = translateError(searchParamsResolved.error);
    return (
      <>
        <ErrorAlert title={error.title} explanation={error.explanation} />
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

function translateError(error: "Callback" | "OAuthSignin"): ErrorMessage {
  switch (error) {
    case "OAuthSignin":
      return {
        title: "Authorization server was unreachable!",
        explanation:
          "Login is impossible because the authorization server is unavailable at this time, please try later, all features should still be avaialabel that don't require authentication.",
      };
    case "Callback":
      return {
        title: "Communication with the backend server was impossible!",
        explanation:
          "Backend server api is unreachable! Although the frontend still functions the backend server is not responding, please visit later!",
      };
    default:
      return {
        title: "Unexpected error occured!",
        explanation: "Please try visiting later!",
      };
  }
}
