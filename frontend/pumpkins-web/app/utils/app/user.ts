import type { UserRole } from "~~/shared/types/user"

export const defaultUserRole = (): UserRole => {
  return "reviewer"
}
