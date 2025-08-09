import HalfFilledStarIcon from "@/components/star/HalfFilledStarIcon";
import StarIcon from "@/components/star/StarIcon";

interface RatingAverageViewParams {
  stars: number;
  className?: string;
  fillColor?: string;
}

function Star({
  starCount,
  starIndex,
  fillColor,
}: {
  starCount: number;
  starIndex: number;
  fillColor?: string;
}) {
  if (starCount >= starIndex + 1) {
    return <StarIcon fillColor={fillColor} />;
  }
  if (starCount < starIndex + 1 && starCount > starIndex + 0.5) {
    return <HalfFilledStarIcon fillColor={fillColor} />;
  }
  if (starCount < starIndex + 1) {
    return <StarIcon fillColor="var(--color-gray-200)" />;
  }
}

export default function RatingAverageView({
  stars,
  className = "",
  fillColor,
}: RatingAverageViewParams) {
  return (
    <>
      <div className={`flex flex-row gap-3 ${className}`}>
        {Array.from({ length: 5 }, (_, index) => (
          <Star
            starCount={stars}
            starIndex={index}
            key={index}
            fillColor={fillColor}
          />
        ))}
      </div>
    </>
  );
}
