"use server";

import { coreClient } from "@/api-client";
import { withUser } from "@/lib";
import z from "zod";

const profileModel = z.object({
  email: z.email("Provide a valid e-mail address!"),
  fullName: z
    .string()
    .refine(
      (name) => {
        const nameParts = name.split(" ");
        return nameParts.length > 1 && !nameParts.some((part) => part === "");
      },
      {
        error: "Please provide a valid name!",
      }
    )
    .includes(" ", { error: "Please provide your full name!" }),
  displayName: z
    .string()
    .refine((displayName) => displayName.trim() === displayName, {
      error: "A valid display name cannot be surrounded by whitespaces!",
    }),
});

export type UpdateProfileErrorType = ReturnType<
  typeof z.flattenError<z.infer<typeof profileModel>>
>;

export type UpdateProfileState = {
  error?: UpdateProfileErrorType;
  data: z.infer<typeof profileModel>;
  updateSucceeded?: boolean;
};

export async function updateProfile(
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

  const clientCallResult = await coreClient.PUT(
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
