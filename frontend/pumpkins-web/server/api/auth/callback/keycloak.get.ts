export default defineOAuthKeycloakEventHandler({
  async onSuccess(event, { user: kcUser, tokens }) {

    const { $backendCoreApi: $api } = useNitroApp();

    let userNotFound = false;
    let role: UserRole = "REVIEWER";

    // Fetch role from backend
    try {
      role = await $api("/users/{username}/role", { path: { username: kcUser.preferred_username }, headers: { Authorization: `Bearer ${tokens.access_token}` }});
    } catch (error: any) {
      if (error.statusCode === 404) {
        userNotFound = true;
      } else {
        throw error;
      }
    }

    if (userNotFound) {
      // Register user in backend (idempotent — creates if not exists)
      await $api("/users", { method: "POST", headers: { Authorization: `Bearer ${tokens.access_token}` } })
    }

    await setUserSession(event, {
      user: {
        username: kcUser.preferred_username,
        email: kcUser.email,
        displayName: kcUser.display_name ?? kcUser.preferred_username,
        firstName: kcUser.first_name,
        lastName: kcUser.last_name,
        role: role as UserRole
      },
      secure: {
        accessToken: tokens.access_token,
        refreshToken: tokens.refresh_token,
        // expires_in is in seconds; convert to ms timestamp
        accessTokenExpiresAt: Date.now() + (tokens.expires_in ?? 900) * 1000
      }
    })

    return sendRedirect(event, '/')
  },
  onError(event, error) {
    console.error('Keycloak OAuth error:', error)
    return sendRedirect(event, '/')
  }
})