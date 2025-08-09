import Label from "@/components/Label";
import PeopleCounter from "@/components/PeopleCounter";
import { Rating } from "@/model/Ratings";

interface SimplifiedRatingViewParams {
  rating: Rating;
  mostSimplified?: boolean;
}

export default function SimplifiedRatingView({
  rating,
  mostSimplified = false,
}: SimplifiedRatingViewParams) {
  const bgColor = "var(--color-blue-100)";
  const gap = mostSimplified ? "gap-2" : "gap-3";

  return (
    <div
      className="relative rounded-md border-[1px] border-gray-30"
      style={{
        background: `linear-gradient(90deg, ${bgColor} 0%, ${bgColor} ${
          rating.average * 20
        }%, rgba(0,0,0,0) ${rating.average * 20 + 1}%)`,
      }}
    >
      <div className={`flex flex-row p-2 ${gap} justify-between items-center`}>
        {mostSimplified ? (
          <>
            <b>{rating.label}</b>
            <b>{rating.average}</b>
            <PeopleCounter count={rating.raterCount} />
          </>
        ) : (
          <>
            <Label text={rating.label} />
            <span className="text-xl">
              <b>{rating.average}</b>/5
            </span>
            <PeopleCounter count={rating.raterCount} />
          </>
        )}
      </div>
    </div>
  );
}
