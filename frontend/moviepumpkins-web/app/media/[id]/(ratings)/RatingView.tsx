import Label from "@/components/Label";
import PeopleCounter from "@/components/PeopleCounter";
import { Rating } from "@/model/Ratings";
import RatingAverageView from "../../../../components/star/FiveStars";

interface RatingViewParams {
  rating: Rating;
  golden?: boolean;
  className?: string;
}

export default function RatingView({
  rating,
  golden = false,
  className = "",
}: RatingViewParams) {
  return (
    <>
      <div className={`sm:w-1/2 lg:w-1/3 pr-3 pb-3 ${className}`}>
        <div
          className={`flex flex-col flex-wrap p-3 ${
            golden ? "border-amber-300" : "border-gray-300"
          } border-[1px] rounded ${golden ? "bg-amber-50" : ""}`}
        >
          <div className="flex flex-row items-center">
            <Label variant={golden ? "gold" : undefined} text={rating.label} />
            <div className="flex flex-row grow right-4 gap-2 items-end justify-end">
              <span className="text-2xl">
                <b>
                  {rating.average.toLocaleString(undefined, {
                    minimumFractionDigits: 1,
                  })}
                </b>
                /5
              </span>
              <PeopleCounter count={rating.raterCount} />
            </div>
          </div>
          <RatingAverageView
            stars={rating.average}
            fillColor={golden ? "var(--color-amber-400)" : undefined}
            className="sm:w-full"
          />
        </div>
      </div>
    </>
  );
}
