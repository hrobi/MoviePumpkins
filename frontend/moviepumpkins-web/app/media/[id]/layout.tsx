import NotificationMessage from "@/components/message-box/NotificationMessage";
import { MediaDetails } from "@/model/MediaDetails";
import { Ratings } from "@/model/Ratings";
import { Review } from "@/model/Review";
import RatingsView from "./(ratings)/RatingsView";
import ReviewsView from "./(reviews)/ReviewsView";
import MediaDetailsView from "./MediaDetailsView";

import DUNE_MOVIE_DETAILS from "@/public/mock-data/media/12/dune-MovieDetails.json";
import DUNE_RATINGS from "@/public/mock-data/media/12/dune-Ratings.json";
import DUNE_REVIEWS from "@/public/mock-data/media/12/dune-Reviews.json";

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
        <div className="xl:w-2/3 mx-auto rounded-md p-8 mt-5">
          <NotificationMessage title="Ooops! Media does not exist">
            <p>
              The media you were looking for with id {id} appears not to exist!
            </p>
          </NotificationMessage>
        </div>
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
