import { logoutFromKeycloakWithRefreshToken } from "~~/server/utils/auth"

export default defineEventHandler(async (event) => {
  const secure = (await getUserSession(event)).secure;
  if (!secure) {
    return;
  }
  await logoutFromKeycloakWithRefreshToken(event, secure.refreshToken)
  await clearUserSession(event)
  return sendRedirect(event, '/')
})
