import { authOptions } from "@/app/api/auth/[...nextauth]/route";
import { getServerSession, Session } from "next-auth";

export function take<T>(array: T[], n: number): T[] {
  return array.filter((_, index) => index < n);
}

export function drop<T>(array: T[], n: number): T[] {
  return array.filter((_, index) => index >= n);
}

export function lineBreakIntoParagraph(text: string) {
  return (
    <>
      {text.split("\n").map((line, index) => (
        <p key={index}>{line}</p>
      ))}
    </>
  );
}

export function isBetweenInc(num: number, moreThan: number, lessThan: number) {
  return num >= moreThan && num <= lessThan;
}

export function formatCount(count: number): string {
  const countStringLen = count.toString().length;
  const formatDigits = (num: number) =>
    num.toLocaleString(undefined, { maximumFractionDigits: 2 });
  return countStringLen > 1000_000
    ? formatDigits(count / 1000_000) + "M"
    : isBetweenInc(countStringLen, 1000, 100_000)
    ? formatDigits(count / 1000) + "K"
    : count.toString();
}

export async function getUser(): Promise<Session["user"]> {
  const session = await getServerSession(authOptions);
  return session?.user;
}

export async function withUser<T>(
  produce: (
    user: NonNullable<Session["user"]>,
    authParams: () => { header: { Authorization: string } }
  ) => T
): Promise<T> {
  const user = (await getUser())!;
  return produce(user, () => ({
    header: {
      Authorization: `bearer ${user!.accessToken}`,
    },
  }));
}
