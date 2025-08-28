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
    id_token?: string;
    access_token?: string;
    provider?: string;
    username?: string;
    role?: UserRole;
  }
}

declare module "next-auth" {
  interface Profile {
    preferred_username?: string;
  }

  interface Session {
    user?: SessionUser;
  }
}
