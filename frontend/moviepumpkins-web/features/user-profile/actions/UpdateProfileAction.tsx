"use server";

import {
    updateUserProfile,
    UpdateUserProfileErrors,
    UpdateUserProfileResponses,
} from "@/configuration/api-client/clients/pumpkins-core";
import { getUser, unexpectedNoAuthError } from "@/features/app/lib";
import { ActionStateUpdateUserProfile } from "@/features/user-profile/model";
import { revalidateTag } from "next/cache";
import { cases, matcherReturnType, matchHttpResponse, total } from "toori";

export type UpdateProfileAction = typeof updateProfileAction;

export async function updateProfileAction(
    prevState: ActionStateUpdateUserProfile,
    formData: FormData,
): Promise<ActionStateUpdateUserProfile> {
    const currentUser = await getUser();
    if (!currentUser) {
        throw new Error("updateProfileAction can only be performed if user is logged in!");
    }

    const rawFormData = {
        email: formData.get("email") as string,
        fullName: formData.get("fullName") as string,
        displayName: formData.get("displayName") as string,
    };

    const {error, response, data} = await updateUserProfile({
        path: {username: currentUser.username},
        body: rawFormData,
    });

    return matchHttpResponse<UpdateUserProfileErrors & UpdateUserProfileResponses>({
        error,
        data,
        response,
    })(
        matcherReturnType<ActionStateUpdateUserProfile>(),
        cases(401, 403, ({response: {status}}) => {
            throw unexpectedNoAuthError(status);
        }),
        total({
            400: ({body: {violations}}) => {
                return {
                    state: {status: "requestBodyError", errors: violations},
                    data: rawFormData,
                };
            },
            204: () => {
                revalidateTag("getUserProfile");
                return {state: {status: "ok"}, data: rawFormData};
            },
        }),
    );
}
