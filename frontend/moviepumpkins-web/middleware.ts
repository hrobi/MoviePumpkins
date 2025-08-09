import withAuth from "next-auth/middleware";

const protectedRoutes: string[] = [];

export default withAuth({
    callbacks: {
        async authorized({req, token}) {
            return protectedRoutes.indexOf(req.nextUrl.pathname) == -1 || !!token;
        }
    },
    pages: {
        signIn: "/auth/sign-in",
        error: "/auth/error",
    },
});