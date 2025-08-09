import { Star as FullStar } from "lucide-react";

interface StarIconParams {
  fillColor?: string;
  size?: string;
}

export default function StarIcon({
  size = "2.5rem",
  fillColor = "var(--color-secondary)",
}: StarIconParams) {
  return (
    <>
      <div style={{ height: size, width: size, position: "relative" }}>
        <FullStar
          className="absolute"
          size={size}
          fill={fillColor}
          strokeWidth={0}
        />
      </div>
    </>
  );
}
