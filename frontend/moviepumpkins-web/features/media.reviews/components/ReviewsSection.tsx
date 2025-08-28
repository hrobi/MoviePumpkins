"use client";

import { Section } from "@/features/app.scaffold/components";
import { VSpacer } from "@/features/app/components";
import { ReviewBox } from "@/features/media.reviews/components";
import { Review } from "@/features/media.reviews/model";
import CommentIcon from "@mui/icons-material/Comment";
import { Button, Divider, Pagination, Stack } from "@mui/material";

export interface ReviewsViewProps {
  reviews: Review[];
}

export function ReviewsSection({ reviews }: ReviewsViewProps) {
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
