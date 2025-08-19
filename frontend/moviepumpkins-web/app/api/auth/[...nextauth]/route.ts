import { coreClient } from "@/api-client";
import { UserRole } from "@/model/User";
import NextAuth, { AuthOptions } from "next-auth";
import { JWT } from "next-auth/jwt";
import Keycloak, { KeycloakProfile } from "next-auth/providers/keycloak";
import { OAuthConfig } from "next-auth/providers/oauth";

const SIGN_IN_PAGE_ROUTE = "/auth/signin";
const ERROR_PAGE_ROUTE = "/auth/error";

declare module "next-auth/jwt" {
  interface JWT {
    id_token?: string;
    access_token?: string;
    provider?: string;
    username?: string;
    role?: UserRole;
  }
}

declare module "next-auth" {
  interface Profile {
    preferred_username?: string;
  }

  interface Session {
    user?: {
      name: string;
      username: string;
      email: string;
      role: UserRole;
      accessToken: string;
    };
  }
}

async function signOutOfKeycloak(token: JWT) {
  const issuerUrl = (
    authOptions.providers.find(
      (p) => p.id === "keycloak"
    ) as OAuthConfig<KeycloakProfile>
  ).options!.issuer!;
  const logOutUrl = new URL(`${issuerUrl}/protocol/openid-connect/logout`);
  logOutUrl.searchParams.set("id_token_hint", token.id_token!);
  await fetch(logOutUrl);
}

async function fetchUserOrThrow({ token }: { token: JWT }) {
  const user = await coreClient.GET("/users/{username}/protected", {
    params: {
      path: { username: token.username! },
      header: { Authorization: `Bearer ${token.access_token}` },
    },
    cache: "no-cache",
  });
  if (user.error) {
    throw new Error(
      `Communication with the core server was unsuccessful, status code: ${user.response.status}`
    );
  }
  return user.data;
}

export const authOptions: AuthOptions = {
  secret: process.env.NEXTAUTH_SECRET,
  providers: [
    Keycloak({
      issuer: process.env.OAUTH_ISS,
      clientId: process.env.OAUTH_CLIENT_ID!,
      clientSecret: process.env.OAUTH_CLIENT_SECRET!,
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile }) {
      if (account) {
        token.id_token = account.id_token;
        token.access_token = account.access_token;
        token.provider = account.provider;
      }
      if (profile) {
        token.username = profile.preferred_username;
      }

      try {
        const user = await fetchUserOrThrow({ token });
        token.role = user.role;
      } catch (ex) {
        await signOutOfKeycloak(token);
        throw ex;
      }

      return token;
    },
    async session({ token, session }) {
      if (session.user && token.role) {
        session.user.role = token.role;
        session.user.username = token.username!;
        session.user.accessToken = token.access_token!;
      }
      return session;
    },
  },
  events: {
    async signOut({ token }) {
      if (token.provider === "keycloak") {
        await signOutOfKeycloak(token);
      }
    },
  },
  pages: {
    signIn: SIGN_IN_PAGE_ROUTE,
    error: ERROR_PAGE_ROUTE,
  },
};
const handler = NextAuth(authOptions);

export { handler as GET, handler as POST };
