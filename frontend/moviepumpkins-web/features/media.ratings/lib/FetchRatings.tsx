import { Ratings } from "@/features/media.ratings/model";
import DUNE_RATINGS from "@/public/mock-data/media/12/dune-Ratings.json";

export async function fetchRatings(id: number): Promise<Ratings> {
  return DUNE_RATINGS;
}
