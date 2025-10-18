import {
  getMediaById,
  GetMediaByIdErrors,
  GetMovieResponse,
  GetSeriesResponse,
} from "@/_generated/configuration/api-client/clients/pumpkins-core";
import { MediaDetails } from "@/features/media/model";
import { cases, matcherReturnType, matchOn } from "toori";
import { catchErrorResponse, matchingOkResponse, matchResolvedResponse } from "toori/http";

type MediaDetailsFetch =
    | { details: MediaDetails; notFound: false }
    | { details: null; poster?: Blob; notFound: true };

export async function fetchMediaDetailsById(id: number): Promise<MediaDetailsFetch> {
    const mediaResponse = await getMediaById({path: {id}});

    const mediaDetailsFetch = matchResolvedResponse<
        { 200: GetMovieResponse | GetSeriesResponse } & GetMediaByIdErrors
    >(mediaResponse)(
        matcherReturnType<MediaDetailsFetch>(),
        matchingOkResponse(({body: details}) => {
            return {notFound: false, details};
        }),
        catchErrorResponse((error) =>
            matchOn(error, "status")(
                matcherReturnType<MediaDetailsFetch>(),
                cases("NotFound", () => ({notFound: true, details: null})),
            ),
        ),
    );

    if (!mediaDetailsFetch.notFound) {
        return mediaDetailsFetch;
    }

    return {notFound: true, details: null};
}
