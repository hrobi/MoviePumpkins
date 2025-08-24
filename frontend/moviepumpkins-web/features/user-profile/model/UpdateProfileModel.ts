import z from "zod";

export const profileModel = z.object({
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
