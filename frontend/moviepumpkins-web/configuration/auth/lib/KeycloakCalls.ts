import { AuthOptions } from "next-auth";
import { JWT } from "next-auth/jwt";
import { KeycloakProfile } from "next-auth/providers/keycloak";
import { OAuthConfig } from "next-auth/providers/oauth";

export async function signOutOfKeycloak(idToken: string, authOptions: AuthOptions) {
  const issuerUrl = (
    authOptions.providers.find((p) => p.id === "keycloak") as OAuthConfig<KeycloakProfile>
  ).options!.issuer!;
  const logOutUrl = new URL(`${issuerUrl}/protocol/openid-connect/logout`);
  logOutUrl.searchParams.set("id_token_hint", idToken);
  await fetch(logOutUrl);
}

export async function refreshAccessToken(
  token: JWT,
  authOptions: AuthOptions
): Promise<JWT> {
  const options = (
    authOptions.providers.find((p) => p.id === "keycloak") as OAuthConfig<KeycloakProfile>
  ).options;

  try {
    const url = `${options!.issuer}/protocol/openid-connect/token`;

    const response = await fetch(url, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      method: "POST",
      body: new URLSearchParams({
        client_id: options!.clientId,
        client_secret: options!.clientSecret,
        grant_type: "refresh_token",
        refresh_token: token.refreshToken,
      }),
    });

    const refreshedTokens = await response.json();

    return {
      ...token,
      accessToken: refreshedTokens.access_token,
      accessTokenExpiresAt: Date.now() + refreshedTokens.expires_in * 1000,
      refreshToken: refreshedTokens.refresh_token ?? token.refreshToken,
    };
  } catch (ex) {
    console.error(ex);

    return {
      ...token,
      error: "RefreshTokenError",
    };
  }
}
