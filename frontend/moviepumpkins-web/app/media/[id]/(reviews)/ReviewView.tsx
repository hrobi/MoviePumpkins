import ExpandableBox from "@/components/ExpandableBox";
import Label from "@/components/Label";
import StarIcon from "@/components/star/StarIcon";
import { REM } from "@/constants";
import { lineBreakIntoParagraph } from "@/lib";
import { Review } from "@/model/Review";
import { ThumbsDownIcon, ThumbsUpIcon, XIcon } from "lucide-react";
import Link from "next/link";
import * as React from "react";

export interface ReviewViewProps {
  review: Review;
}

function ReviewHeader({
  title,
  rating,
  userDisplayName,
  username,
}: {
  title: React.ReactNode;
  rating: number;
  userDisplayName: String;
  username: string;
}) {
  return (
    <>
      <div className="flex flex-row justify-between">
        <h2>{title}</h2>
        <div className="flex flex-row items-center gap-2">
          <StarIcon />
          <span className="text-2xl">
            <b>
              {rating.toLocaleString(undefined, {
                minimumFractionDigits: 1,
              })}
            </b>
            /5
          </span>
        </div>
      </div>
      <div className="gap-1">
        <span className="font-bold">{userDisplayName}</span>
        <Link href={`/@${username}`} className="text-sm">
          @{username}
        </Link>
      </div>
    </>
  );
}

function LikeDislikeReport({
  likes,
  dislikes,
}: {
  likes: number;
  dislikes: number;
}) {
  return (
    <>
      <div className="mt-2 flex flex-row">
        <button className="flex flex-row gap-2 items-start p-2 text-sky-700 pr-2 cursor-pointer svg-fill-sky-hover-container">
          <ThumbsUpIcon stroke="var(--color-sky-700)" />
          {likes}
        </button>
        <button className="flex flex-row gap-2 items-start p-2 text-red-700 pl-2 cursor-pointer svg-fill-red-hover-container">
          <ThumbsDownIcon stroke="var(--color-red-700)" />
          {dislikes}
        </button>
        <button className="flex flex-row gap-1 p-2 items-center text-red-700 cursor-pointer">
          <XIcon color="var(--color-red-700)" />
          Report
        </button>
      </div>
    </>
  );
}

export default function ReviewView({ review }: ReviewViewProps) {
  const contentContainerElement = React.useRef<HTMLDivElement | null>(null);

  return (
    <article className="flex flex-col border-t-[1px] border-gray-200 py-4">
      <ReviewHeader
        title={review.title}
        rating={review.rating}
        userDisplayName={review.userDisplayName}
        username={review.username}
      />
      {!review.spoilerFree && <Label variant="alert" text="spoiler" />}
      <ExpandableBox baseHeight={review.spoilerFree ? 15 * REM : 0}>
        <div ref={contentContainerElement}>
          {lineBreakIntoParagraph(review.content)}
          <LikeDislikeReport
            likes={review.usefulness.likes}
            dislikes={review.usefulness.dislikes}
          />
        </div>
      </ExpandableBox>
    </article>
  );
}
