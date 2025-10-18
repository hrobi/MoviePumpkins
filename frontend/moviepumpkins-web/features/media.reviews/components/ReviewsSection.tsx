"use client";

import { Section } from "@/features/app.scaffold/components";
import { VSpacer } from "@/features/app/components";
import { usePagination } from "@/features/app/hooks/UsePagination";
import { ReviewBox } from "@/features/media.reviews/components";
import { Reviews } from "@/features/media.reviews/model";
import CommentIcon from "@mui/icons-material/Comment";
import { Button, Divider, Pagination, Stack } from "@mui/material";

export interface ReviewsViewProps {
    reviews: Reviews;
}

export function ReviewsSection({
                                   reviews: {currentPageReviews: reviews, pageCount, page},
                               }: ReviewsViewProps) {
    const {navigateToPage} = usePagination("reviewPage");
    return (
        <Section
            title="Reviews"
            additionalButton={<Button startIcon={<CommentIcon/>}>Review</Button>}
        >
            {reviews.map((review) => (
                <div key={review.id}>
                    <ReviewBox review={review}/>
                    <Divider sx={{marginTop: 2, marginBottom: 2}}/>
                </div>
            ))}
            <VSpacer size={2}/>
            {pageCount > 0 && (
                <Stack direction="row" justifyContent="center">
                    <Pagination
                        count={pageCount}
                        shape="rounded"
                        page={page}
                        onChange={(_, newPage) => navigateToPage(newPage)}
                    />
                </Stack>
            )}
            {pageCount == 0 && "Be the first to review!"}
        </Section>
    );
}
