"use client";

import AddOrModifyButton from "@/components/AddOrModifiyButton";
import Counter from "@/components/Counter";
import Label from "@/components/Label";
import Section from "@/components/Section";
import { lineBreakIntoParagraph } from "@/lib";
import { Awards, MediaDetails } from "@/model/MediaDetails";
import OverallAverageRatingView from "./OverallAverageRatingView";

interface MediaDetailsViewParams {
  details: MediaDetails;
}

function Subsection({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <div className="border-b-[1px] border-gray-200 pb-2">
      <Label text={label} className="mr-3" />
      {children}
    </div>
  );
}

function AwardsSubsection({ awards }: { awards: Awards }) {
  return (
    <>
      <div className="flex flex-row border-b-[1px] gap-3 border-gray-200 pb-2 flex-wrap items-baseline">
        <Label text="Awards" />
        {awards.totalWinCount && (
          <Counter variant="gold" title="Wins" count={awards.totalWinCount} />
        )}
        {awards.totalNominationCount && (
          <Counter
            variant="green"
            title="Nominations"
            count={awards.totalNominationCount}
          />
        )}
        {awards.namedAwards.map(({ type, count }, index) => (
          <Counter
            variant="blue"
            title={`${type}(s)`}
            count={count}
            key={index}
          />
        ))}
      </div>
    </>
  );
}

export default function MediaDetailsView({ details }: MediaDetailsViewParams) {
  return (
    <>
      <Section
        title={details.title}
        additionalButton={<AddOrModifyButton text="modify" variant="modify" />}
        className="relative"
      >
        {details.averageRating && (
          <OverallAverageRatingView rating={details.averageRating} />
        )}
        <div className="flex lg:flex-row flex-col gap-3 items-center lg:items-start">
          <img
            className="rounded-md"
            width="300px"
            height="200px"
            src={details.pictureHref}
          />
          <div className="flex flex-col gap-3">
            <div className="border-b-[1px] border-gray-200 pb-2">
              {lineBreakIntoParagraph(details.shortDescription)}
            </div>
            {details.directors.length > 0 && (
              <Subsection label="Director(s)">
                {details.directors.join(", ")}
              </Subsection>
            )}
            {details.actors.length > 0 && (
              <Subsection label="Actors">
                {details.actors.join(", ")}
              </Subsection>
            )}
            {details.writers.length > 0 && (
              <Subsection label="Writers">
                {details.writers.join(", ")}
              </Subsection>
            )}
            {details.awards && <AwardsSubsection awards={details.awards} />}
          </div>
        </div>
      </Section>
    </>
  );
}
