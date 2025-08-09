import { Star as FullStar, StarHalf } from "lucide-react";

interface HalfFilledStarIconParams {
  size?: string;
  fillColor?: string;
}

export default function HalfFilledStarIcon({
  size = "2.5rem",
  fillColor = "var(--color-secondary)",
}: HalfFilledStarIconParams) {
  return (
    <>
      <div style={{ height: size, width: size, position: "relative" }}>
        <FullStar
          className="absolute"
          size={size}
          fill="var(--color-gray-200)"
          strokeWidth={0}
        />
        <StarHalf
          className="absolute"
          size={size}
          fill={fillColor}
          strokeWidth={0}
        />
      </div>
    </>
  );
}
