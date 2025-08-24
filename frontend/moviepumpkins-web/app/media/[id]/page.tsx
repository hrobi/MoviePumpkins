import { RatingsSection } from "@/features/media.ratings/components";
import { fetchRatings } from "@/features/media.ratings/lib";
import { ReviewsSection } from "@/features/media.reviews/components";
import { fetchReviews } from "@/features/media.reviews/lib";
import {
  MediaDetailsSection,
  MediaNotFoundAlert,
} from "@/features/media/components";
import { fetchMediaDetailsById } from "@/features/media/lib";

export default async function Page({
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
      <MediaDetailsSection details={details} />
      <RatingsSection id={id} ratings={ratings} />
      <ReviewsSection reviews={reviews} />
      {children}
    </>
  );
}
