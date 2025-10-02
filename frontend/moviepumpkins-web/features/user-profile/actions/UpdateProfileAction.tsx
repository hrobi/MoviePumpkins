"use server";

import {
  updateUserProfile,
  UpdateUserProfileErrors,
  UpdateUserProfileResponses,
} from "@/configuration/api-client/clients/pumpkins-core";
import { getUser } from "@/features/app/lib";
import { UnexpectedAuthErrorResponseError } from "@/features/app/lib/Errors";
import { ActionStateUpdateUserProfile } from "@/features/user-profile/model";
import { revalidateTag } from "next/cache";
import { cases, matcherReturnType, matchOn } from "toori";
import {
  catchErrorResponse,
  matchingOkResponse,
  matchResolvedResponse,
} from "toori/http";

export type UpdateProfileAction = typeof updateProfileAction;

export async function updateProfileAction(
  prevState: ActionStateUpdateUserProfile,
  formData: FormData
): Promise<ActionStateUpdateUserProfile> {
  const currentUser = await getUser();
  console.log(currentUser);
  if (!currentUser) {
    throw new Error("updateProfileAction can only be performed if user is logged in!");
  }

  const rawFormData = {
    email: formData.get("email") as string,
    fullName: formData.get("fullName") as string,
    displayName: formData.get("displayName") as string,
  };

  const { error, response, data } = await updateUserProfile({
    path: { username: currentUser.username },
    body: rawFormData,
  });

  console.log(response.status);

  return matchResolvedResponse<UpdateUserProfileErrors & UpdateUserProfileResponses>({
    error,
    data,
    response,
  })(
    matcherReturnType<ActionStateUpdateUserProfile>(),
    matchingOkResponse(() => {
      revalidateTag("getUserProfile");
      return { data: rawFormData, state: { status: "ok" } };
    }),
    catchErrorResponse((err) =>
      matchOn(err, "status")(
        matcherReturnType<ActionStateUpdateUserProfile>(),
        cases("Unauthorized", "Forbidden", ({ status, response }) => {
          throw new UnexpectedAuthErrorResponseError(
            status.toLocaleLowerCase() as Lowercase<typeof status>
          );
        }),
        cases("BadRequest", ({ body: { violations } }) => ({
          data: rawFormData,
          state: { status: "requestBodyError", errors: violations },
        }))
      )
    )
  );
}
