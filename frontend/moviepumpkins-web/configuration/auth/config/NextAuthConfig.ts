import { fetchUserOrThrow, signOutOfKeycloak } from "@/configuration/auth/lib";
import {
  ERROR_PAGE_ROUTE,
  SIGN_IN_PAGE_ROUTE,
} from "@/configuration/auth/model";
import { AuthOptions } from "next-auth";
import Keycloak from "next-auth/providers/keycloak";

export const nextAuthOptions: AuthOptions = {
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
        await signOutOfKeycloak(token, nextAuthOptions);
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
        await signOutOfKeycloak(token, nextAuthOptions);
      }
    },
  },
  pages: {
    signIn: SIGN_IN_PAGE_ROUTE,
    error: ERROR_PAGE_ROUTE,
  },
};
