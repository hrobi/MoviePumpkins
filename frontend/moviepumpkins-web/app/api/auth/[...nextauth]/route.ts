import NextAuth from "next-auth";
import Keycloak from "next-auth/providers/keycloak";

const handler = NextAuth({
    secret: process.env.NEXTAUTH_SECRET,
    providers: [
        Keycloak({
            issuer: process.env.OAUTH_ISS,
            clientId: process.env.OAUTH_CLIENT_ID,
            clientSecret: process.env.OAUTH_CLIENT_SECRET,
        }),
    ],
});

export { handler as GET, handler as POST };