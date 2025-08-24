import { MediaDetails } from "@/model/MediaDetails";
import { Ratings } from "@/model/Ratings";
import { Review } from "@/model/Review";

import DUNE_MOVIE_DETAILS from "@/public/mock-data/media/12/dune-MovieDetails.json";
import DUNE_RATINGS from "@/public/mock-data/media/12/dune-Ratings.json";
import DUNE_REVIEWS from "@/public/mock-data/media/12/dune-Reviews.json";
import MediaDetailsView from "./_components/MediaDetailsView";
import { MediaNotFoundAlert } from "./_components/MediaNotFoundAlert";
import RatingsView from "./_components/RatingsView";
import ReviewsView from "./_components/ReviewsView";

async function fetchMediaDetailsById(
  id: number
): Promise<
  { details: MediaDetails; notFound: false } | { details: null; notFound: true }
> {
  if (id == 12) {
    return { notFound: false, details: DUNE_MOVIE_DETAILS };
  }
  return { notFound: true, details: null };
}

async function fetchRatings(id: number): Promise<Ratings> {
  return DUNE_RATINGS;
}

async function fetchReviews({}: {
  id: number;
  page: number;
}): Promise<Review[]> {
  return DUNE_REVIEWS;
}

export default async function Layout({
  params,
  children,
}: {
  params: Promise<{ id: number }>;
  children: React.ReactNode;
}) {
  const { id } = await params;
  const { details, notFound } = await fetchMediaDetailsById(id);

  if (notFound) {
    return (
      <>
        <MediaNotFoundAlert id={id} />
      </>
    );
  }

  const ratings = await fetchRatings(id);
  const reviews = await fetchReviews({ id, page: 1 });

  return (
    <>
      <MediaDetailsView details={details} />
      <RatingsView id={id} ratings={ratings} />
      <ReviewsView reviews={reviews} />
      {children}
    </>
  );
}
