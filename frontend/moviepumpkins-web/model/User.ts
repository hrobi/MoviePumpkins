export type UserRole = "supervisor" | "reviewer" | "admin";

export interface UserProfile {
  username: string;
  fullName: string;
  email: string;
  displayName: string;
  role: UserRole;
}
