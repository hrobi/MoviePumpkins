import { UserRole } from "@/configuration/user/model";

export interface SessionUser {
  name: string;
  username: string;
  email: string;
  role: UserRole;
  accessToken: string;
}

declare module "next-auth/jwt" {
  interface JWT {
    idToken: string;
    accessToken: string;
    accessTokenExpiresAt: number;
    refreshToken: string;
    provider: string;
    username: string;
    role: UserRole;
    error?: "RefreshTokenError";
  }
}

declare module "next-auth" {
  interface Profile {
    preferred_username?: string;
  }

  interface Session {
    user?: SessionUser;
    error?: "RefreshTokenError";
  }
}
