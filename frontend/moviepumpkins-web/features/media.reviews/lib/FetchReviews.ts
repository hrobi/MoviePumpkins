import { Review } from "@/features/media.reviews/model";
import DUNE_REVIEWS from "@/public/mock-data/media/12/dune-Reviews.json";

export async function fetchReviews({}: {
  id: number;
  page: number;
}): Promise<Review[]> {
  return DUNE_REVIEWS;
}
