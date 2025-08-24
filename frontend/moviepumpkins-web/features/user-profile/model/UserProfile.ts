import { UserRole } from "@/configuration/user/model";

export interface UserProfile {
  username: string;
  fullName: string;
  email: string;
  displayName: string;
  role: UserRole;
}
