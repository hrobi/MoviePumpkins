export type StateUpdateUserProfile =
    | { status: "ok" }
    | { status: "uninitialized" }
    | {
    status: "requestBodyError";
    errors: { fields: string[]; reason: string }[];
};

interface UserProfileUpdate {
    email: string;
    fullName: string;
    displayName: string;
}

export interface ActionStateUpdateUserProfile {
    data: UserProfileUpdate;
    state: StateUpdateUserProfile;
}
