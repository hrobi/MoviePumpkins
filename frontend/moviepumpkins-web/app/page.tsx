import { nextAuthOptions } from "@/configuration/auth";
import { getServerSession } from "next-auth";

export default async function () {
  const session = await getServerSession(nextAuthOptions);
  return <h1>Hi</h1>;
}
