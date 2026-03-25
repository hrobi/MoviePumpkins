export default defineNuxtRouteMiddleware(_ => {
    if (!useUserSession().loggedIn.value) {
        return navigateTo("/api/auth/callback/keycloak", { external: true });
    }
})
