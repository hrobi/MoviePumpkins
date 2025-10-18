"use client";

import { Section } from "@/features/app.scaffold/components";
import { usePagination } from "@/features/app/hooks/UsePagination";
import { RatingsContentBox } from "@/features/media.ratings/components";
import { Ratings } from "@/features/media.ratings/model";
import HotelClassIcon from "@mui/icons-material/HotelClass";
import { Button, Divider, Pagination, Stack, Typography } from "@mui/material";

interface RatingsViewParams {
    id: number;
    ratings: Ratings;
}

export function RatingsSection({id, ratings}: RatingsViewParams) {
    const {navigateToPage} = usePagination("scorePage");
    return (
        <>
            <Section
                title="Ratings"
                additionalButton={<Button startIcon={<HotelClassIcon/>}>Rate</Button>}
                className="relative"
            >
                {ratings.pageCount == 0 && (
                    <Typography variant="body1">No ratings so far</Typography>
                )}
                {ratings.pageCount > 0 && (
                    <>
                        <RatingsContentBox ratings={ratings.ratings}/>
                        <Divider sx={{marginTop: 2, marginBottom: 2}}/>
                        <Stack direction="row" justifyContent="center">
                            <Pagination
                                count={ratings.pageCount}
                                page={ratings.page}
                                onChange={(_, newPage) => navigateToPage(newPage)}
                            />
                        </Stack>
                    </>
                )}
            </Section>
        </>
    );
}
