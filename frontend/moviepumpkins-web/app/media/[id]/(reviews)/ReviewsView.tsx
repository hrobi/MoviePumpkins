"use client";

import AddOrModifyButton from "@/components/AddOrModifiyButton";
import Section from "@/components/Section";
import { Review } from "@/model/Review";
import ReviewView from "./ReviewView";

export interface ReviewsViewProps {
  reviews: Review[];
}

export default function ReviewsView({ reviews }: ReviewsViewProps) {
  return (
    <Section
      title="Reviews"
      additionalButton={
        <AddOrModifyButton
          href="add-review"
          variant="add"
          text="Add your own"
        />
      }
    >
      {reviews.map((review, index) => (
        <ReviewView review={review} key={review.id} />
      ))}
    </Section>
  );
}
