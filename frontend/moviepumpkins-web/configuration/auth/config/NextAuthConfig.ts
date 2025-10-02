import { pumpkinsClient } from "@/configuration/api-client/clients";
import { refreshAccessToken, signOutOfKeycloak } from "@/configuration/auth/lib";
import { ERROR_PAGE_ROUTE, SIGN_IN_PAGE_ROUTE } from "@/configuration/auth/model";
import { AuthOptions } from "next-auth";
import Keycloak from "next-auth/providers/keycloak";
import { getUserRoleFromBackend } from "../lib/PumpkinsBackendCalls";

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
    async jwt({ token, account, profile, user }) {
      if (account && user && profile) {
        pumpkinsClient.setConfig({ auth: account.access_token });
        try {
          const role = await getUserRoleFromBackend();
          return {
            accessToken: account.access_token!,
            accessTokenExpiresAt: account.expires_at! * 1000,
            idToken: account.id_token!,
            refreshToken: account.refresh_token!,
            provider: account.provider!,
            username: profile.preferred_username!,
            role,
          };
        } catch (ex) {
          signOutOfKeycloak(account.id_token!, nextAuthOptions);
          throw ex;
        }
      }

      pumpkinsClient.setConfig({ auth: token.accessToken });

      if (Date.now() < token.accessTokenExpiresAt) {
        return token;
      }

      const newTokenSet = await refreshAccessToken(token, nextAuthOptions);
      pumpkinsClient.setConfig({ auth: newTokenSet.accessToken });
      return newTokenSet;
    },
    async session({ token, session }) {
      if (session.user && token.role) {
        session.user.username = token.username;
        session.user.role = token.role;
        session.user.accessToken = token.accessToken;
        session.error = token.error;
      }
      return session;
    },
  },
  events: {
    async signOut({ token }) {
      if (token.provider === "keycloak") {
        await signOutOfKeycloak(token.idToken, nextAuthOptions);
      }
    },
  },
  pages: {
    signIn: SIGN_IN_PAGE_ROUTE,
    error: ERROR_PAGE_ROUTE,
  },
};
