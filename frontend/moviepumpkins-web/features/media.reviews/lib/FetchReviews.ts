import {
    getReviewsByPageOrCreator,
    GetReviewsByPageOrCreatorErrors,
    GetReviewsByPageOrCreatorResponses,
} from "@/_generated/configuration/api-client/clients/pumpkins-core";
import { Reviews } from "@/features/media.reviews/model";
import { matcherReturnType, total } from "toori";
import { matchResolvedResponse } from "toori/http";

export async function fetchReviews({
                                       id,
                                       page,
                                   }: {
    id: number;
    page: number;
}): Promise<Reviews> {
    const reviewsResponse = await getReviewsByPageOrCreator({
        path: {id},
        query: {page},
    });
    return matchResolvedResponse<
        GetReviewsByPageOrCreatorErrors & GetReviewsByPageOrCreatorResponses
    >(reviewsResponse)(
        matcherReturnType<Reviews>(),
        total({
            NotFound: () => ({pageCount: 0, currentPageReviews: [], page: 0}),
            OK: ({
                     body: {
                         reviews,
                         pagination: {pageCount},
                     },
                 }) => ({
                currentPageReviews: reviews,
                pageCount: pageCount,
                page: page,
            }),
        }),
    );
}
