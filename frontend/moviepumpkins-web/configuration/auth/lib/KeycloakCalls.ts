import { AuthOptions } from "next-auth";
import { JWT } from "next-auth/jwt";
import { KeycloakProfile } from "next-auth/providers/keycloak";
import { OAuthConfig } from "next-auth/providers/oauth";

export async function signOutOfKeycloak(token: JWT, authOptions: AuthOptions) {
  const issuerUrl = (
    authOptions.providers.find(
      (p) => p.id === "keycloak"
    ) as OAuthConfig<KeycloakProfile>
  ).options!.issuer!;
  const logOutUrl = new URL(`${issuerUrl}/protocol/openid-connect/logout`);
  logOutUrl.searchParams.set("id_token_hint", token.id_token!);
  await fetch(logOutUrl);
}
