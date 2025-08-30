"use server";

import { updateUserProfile } from "@/configuration/api-client/clients/pumpkins-core";
import { getUser, throwFatalError } from "@/features/app/lib";
import {
  UpdateUserProfileActionState,
  UpdateUserProfileState,
} from "@/features/user-profile/model";
import { revalidateTag } from "next/cache";
import { match, P } from "ts-pattern";

export type UpdateProfileAction = typeof updateProfileAction;

export async function updateProfileAction(
  prevState: UpdateUserProfileActionState,
  formData: FormData
): Promise<UpdateUserProfileActionState> {
  const currentUser = await getUser();
  if (!currentUser) {
    throw new Error(
      "updateProfileAction can only be performed if user is logged in!"
    );
  }

  const rawFormData = {
    email: formData.get("email") as string,
    fullName: formData.get("fullName") as string,
    displayName: formData.get("displayName") as string,
  };

  const updateUserProfileResult = await updateUserProfile({
    path: { username: currentUser.username },
    body: rawFormData,
  });

  if (!updateUserProfileResult.error) {
    revalidateTag("getUserProfile");
  }

  return {
    data: rawFormData,
    state: updateUserProfileResult.error
      ? match(updateUserProfileResult.error)
          .with({ status: "FORBIDDEN" }, ({ error }) => {
            throwFatalError(error);
          })
          .with(
            { status: "BAD_REQUEST" },
            ({ errors }): UpdateUserProfileState => ({
              status: "request-body-error",
              errors: errors,
            })
          )
          .with({ reason: P.nonNullable }, (error) => {
            throwFatalError(error);
          })
          .exhaustive()
      : { status: "all-ok" },
  };
}
