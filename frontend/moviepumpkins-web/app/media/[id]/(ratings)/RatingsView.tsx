"use client";

import AddOrModifyButton from "@/components/AddOrModifiyButton";
import ExpandableBox from "@/components/ExpandableBox";
import Section from "@/components/Section";
import { REM } from "@/constants";
import { drop, take } from "@/lib";
import { Ratings } from "@/model/Ratings";
import { useRef } from "react";
import RatingView from "./RatingView";
import SimplifiedRatingView from "./SimplifiedRatingView";

interface RatingsViewParams {
  id: number;
  ratings: Ratings;
}

export default function RatingsView({ id, ratings }: RatingsViewParams) {
  const mostImportantRatings = take(ratings, 9);
  const restOfRatings = drop(ratings, 9);

  const contentContainerElement = useRef<HTMLDivElement | null>(null);

  return (
    <>
      <Section
        title="Ratings"
        additionalButton={
          <AddOrModifyButton
            href={`${id}/add-rating`}
            text="What do you think?"
            variant="add"
          />
        }
        className="relative"
      >
        <ExpandableBox baseHeight={20.625 * REM}>
          <div ref={contentContainerElement}>
            <div className="flex sm:flex-row flex-col flex-wrap">
              {mostImportantRatings.map((rating, index) => (
                <RatingView
                  golden={rating.label === "overall"}
                  rating={rating}
                  key={rating.label}
                />
              ))}
            </div>
            <div className="flex flex-row gap-2 flex-wrap">
              {restOfRatings.map((rating) => (
                <SimplifiedRatingView rating={rating} key={rating.label} />
              ))}
            </div>
          </div>
        </ExpandableBox>
      </Section>
    </>
  );
}
