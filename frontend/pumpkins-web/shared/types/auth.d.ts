import type { UserRole } from './user'

declare module '#auth-utils' {
  interface User {
    username: string
    email: string
    displayName: string
    firstName: string
    lastName: string
    role?: UserRole,
  }

  interface SecureSessionData {
    accessToken: string
    refreshToken: string
    accessTokenExpiresAt: number // Unix timestamp (ms)
  }
}

export {}
