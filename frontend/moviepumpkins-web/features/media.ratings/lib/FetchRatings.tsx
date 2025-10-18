import { getScoresByPage } from "@/_generated/configuration/api-client/clients/pumpkins-core";
import { Ratings } from "@/features/media.ratings/model";

export async function fetchRatings(id: number, page: number): Promise<Ratings> {
    const {data} = await getScoresByPage({path: {id}, query: {page}});
    if (!data) {
        return {page, pageCount: 0, ratings: []};
    }
    return {
        page,
        pageCount: data.pagination.pageCount,
        ratings: data.scores,
    };
}
