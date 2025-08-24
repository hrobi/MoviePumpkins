"use client";

import { UserProfile } from "@/model/User";
import { Alert } from "@mui/material";
import { useActionState } from "react";
import { UpdateProfileAction } from "../actions";

function Field({
  label,
  defaultValue,
  disabled = false,
  error,
  name,
}: {
  label: string;
  defaultValue: string;
  disabled?: boolean;
  name?: string;
  error?: string;
}) {
  return (
    <div className="flex sm:flex-row flex-col py-2">
      <label className="w-1/3">{label}</label>
      <div className="flex flex-col gap-1 w-2/3">
        <input
          className={`w-full ${error ? "input-error" : ""}`}
          type="text"
          name={name}
          defaultValue={defaultValue}
          disabled={disabled}
        />
        {error && <span className="text-sm text-red-500">{error}</span>}
      </div>
    </div>
  );
}

export default function Form({
  profile,
  updateProfileAction,
}: {
  profile: UserProfile;
  updateProfileAction: UpdateProfileAction;
}) {
  const [state, formAction, isPending] = useActionState(updateProfileAction, {
    error: undefined,
    data: profile,
  });

  return (
    <form action={formAction}>
      {state.updateSucceeded && !isPending && (
        <Alert severity="success">Update Succeeded!</Alert>
      )}
      <Field label="Username: " disabled defaultValue={profile.username} />
      <Field
        label="E-mail: "
        name="email"
        error={state.error?.fieldErrors.email?.[0]}
        defaultValue={state.data.email}
      />
      <Field
        label="Name: "
        name="fullName"
        error={state?.error?.fieldErrors.fullName?.[0]}
        defaultValue={state.data.fullName}
      />
      <Field
        label="Display Name: "
        name="displayName"
        error={state.error?.fieldErrors.displayName?.[0]}
        defaultValue={state.data.displayName}
      />
      <Field label="Role: " disabled defaultValue={profile.role} />
      <button className="primary-btn" type="submit" disabled={isPending}>
        Update profile
      </button>
    </form>
  );
}
