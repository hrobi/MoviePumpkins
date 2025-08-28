import { MediaDetails } from "@/features/media/model";
import DUNE_MOVIE_DETAILS from "@/public/mock-data/media/12/dune-MovieDetails.json";

export async function fetchMediaDetailsById(
  id: number
): Promise<
  { details: MediaDetails; notFound: false } | { details: null; notFound: true }
> {
  if (id == 12) {
    return { notFound: false, details: DUNE_MOVIE_DETAILS };
  }
  return { notFound: true, details: null };
}
