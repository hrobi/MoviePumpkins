import type { H3Event } from "#imports";

export const refreshAccessTokenWhenNeeded = async (event: H3Event<globalThis.EventHandlerRequest>) => {
  const session = await getUserSession(event)

  if (!session.secure) {
    return;
  }

  if (session.secure.accessTokenExpiresAt > Date.now()) {
    return;
  }

  const { oauth: { keycloak: { serverUrl, realm, clientId, clientSecret } } } = useRuntimeConfig(event)

  try {
    const tokens = await $fetch<{ access_token: string; refresh_token: string; expires_in: number }>(
      `${serverUrl}/realms/${realm}/protocol/openid-connect/token`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          grant_type: 'refresh_token',
          refresh_token: session.secure.refreshToken,
          client_id: clientId,
          client_secret: clientSecret
        })
      }
    )

    console.log("fetched new access token and refresh token");

    await setUserSession(event, {
      ...session,
      secure: {
        accessToken: tokens.access_token,
        refreshToken: tokens.refresh_token,
        accessTokenExpiresAt: Date.now() + tokens.expires_in * 1000
      }
    });

    console.log("updated user session");
  }
  catch {
    await clearUserSession(event)
    await sendRedirect(event, '/api/auth/callback/keycloak')
  }
}

export const logoutFromKeycloakWithRefreshToken = async (event: H3Event<globalThis.EventHandlerRequest>, refreshToken: string) => {
  const { oauth: { keycloak: { serverUrl, realm, clientId, clientSecret } }, apiBaseUrl } = useRuntimeConfig(event);
  await $fetch(`${serverUrl}/realms/${realm}/protocol/openid-connect/logout`, { 
    method: "POST",
    headers: { 
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: new URLSearchParams({
      client_id: clientId,
      client_secret: clientSecret,
      refresh_token: refreshToken,
      post_logout_redirect: apiBaseUrl
    })
  });
}