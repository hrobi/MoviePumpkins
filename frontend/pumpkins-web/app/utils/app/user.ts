import type { UserRole } from "~/types/user"

export const defaultUserRole = (): UserRole => {
  return "reviewer"
}
