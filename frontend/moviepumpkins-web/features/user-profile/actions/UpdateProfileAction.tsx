"use server";

import { pumpkinsCoreClient } from "@/configuration/api-client";
import { withUser } from "@/features/app/lib";
import {
  profileModel,
  UpdateProfileState,
} from "@/features/user-profile/model";
import z from "zod";

export type UpdateProfileAction = typeof updateProfileAction;

export async function updateProfileAction(
  prevState: UpdateProfileState,
  formData: FormData
): Promise<UpdateProfileState> {
  const rawFormData = {
    email: formData.get("email") as string,
    fullName: formData.get("fullName") as string,
    displayName: formData.get("displayName") as string,
  };

  const error = profileModel.safeParse(rawFormData).error;

  if (error) {
    return { error: z.flattenError(error), data: rawFormData };
  }

  const clientCallResult = await pumpkinsCoreClient.PUT(
    "/users/{username}/profile",
    await withUser((user, authParams) => ({
      params: {
        ...authParams(),
        path: { username: user.username },
      },
      body: rawFormData,
    }))
  );

  if (!clientCallResult.response.ok) {
    throw Error(
      `Backend server response not ok: ${JSON.stringify(
        clientCallResult.response
      )}`
    );
  }

  return { data: rawFormData, updateSucceeded: true };
}
