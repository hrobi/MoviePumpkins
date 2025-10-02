import {
  createOwnUser,
  CreateOwnUserErrors,
  CreateOwnUserResponses,
} from "@/_generated/configuration/api-client/clients/pumpkins-core";
import { UnexpectedAuthErrorResponseError } from "@/features/app/lib/Errors";
import { matchOn, total } from "toori";
import {
  catchErrorResponse,
  matchingOkResponse,
  matchResolvedResponse,
} from "toori/http";

export const getUserRoleFromBackend = async () => {
  return matchResolvedResponse<CreateOwnUserResponses & CreateOwnUserErrors>(
    await createOwnUser()
  )(
    matchingOkResponse(({ body: { role } }) => role),
    catchErrorResponse((err) =>
      matchOn(
        err,
        "status"
      )(
        total({
          Unauthorized: ({ status }) => {
            throw new UnexpectedAuthErrorResponseError(
              status as Lowercase<typeof status>
            );
          },
        })
      )
    )
  );
};
