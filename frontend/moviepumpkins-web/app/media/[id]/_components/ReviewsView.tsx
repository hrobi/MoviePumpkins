"use client";

import ReviewBox from "@/components/review/ReviewBox";
import Section from "@/components/Section";
import { VSpacer } from "@/components/Spacer";
import { Review } from "@/model/Review";
import CommentIcon from "@mui/icons-material/Comment";
import { Button, Divider, Pagination, Stack } from "@mui/material";

export interface ReviewsViewProps {
  reviews: Review[];
}

export default function ReviewsView({ reviews }: ReviewsViewProps) {
  return (
    <Section
      title="Reviews"
      additionalButton={<Button startIcon={<CommentIcon />}>Review</Button>}
    >
      {reviews.map((review, index) => (
        <div key={index}>
          <ReviewBox review={review} />
          <Divider sx={{ marginTop: 2, marginBottom: 2 }} />
        </div>
      ))}
      <VSpacer size={2} />
      <Stack direction="row" justifyContent="center">
        <Pagination count={10} shape="rounded" />
      </Stack>
    </Section>
  );
}
