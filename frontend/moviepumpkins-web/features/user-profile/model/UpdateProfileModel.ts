export type UpdateUserProfileState =
  | { status: "all-ok" }
  | { status: "uninitialized" }
  | {
      status: "request-body-error";
      errors: { fields: string[]; reason: string }[];
    };

interface UserProfileUpdate {
  email: string;
  fullName: string;
  displayName: string;
}

export interface UpdateUserProfileActionState {
  data: UserProfileUpdate;
  state: UpdateUserProfileState;
}
