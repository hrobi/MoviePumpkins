import { refreshAccessTokenWhenNeeded } from "~~/server/utils/auth";

export default defineEventHandler(async (event) => {
  const session = await getUserSession(event);

  const actualPath = event.path.substring("/proxy/".length);
  console.log(actualPath)

  if (session.secure) {
    if (session.secure.accessTokenExpiresAt < Date.now()) {
      await refreshAccessTokenWhenNeeded(event);
    }

    return proxyRequest(
      event, 
      `http://localhost:8080/${actualPath}`,
      session.secure 
        ? {
          headers: {
            Authorization: `Bearer ${session.secure.accessToken}`
          }
        } 
        : undefined
    );
  }

  return proxyRequest(
    event, 
    `http://localhost:8080/${actualPath}`,
  );
})
