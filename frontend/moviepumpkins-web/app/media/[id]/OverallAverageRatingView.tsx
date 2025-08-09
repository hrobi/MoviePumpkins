import PeopleCounter from "@/components/PeopleCounter";
import StarIcon from "@/components/star/StarIcon";
import { Rating } from "@/model/Ratings";

interface OverallAverageRatingViewParams {
  rating: Omit<Rating, "label">;
}

export default function OverallAverageRatingView({
  rating,
}: OverallAverageRatingViewParams) {
  return (
    <>
      <div className="flex flex-row gap-2 absolute right-8 top-5 items-center">
        <StarIcon />
        <div className="flex flex-col">
          <span className="text-2xl">
            <b>{rating.average}</b>/5
          </span>
          <PeopleCounter count={rating.raterCount} />
        </div>
      </div>
    </>
  );
}
