"use client";

import { Section } from "@/features/app.scaffold/components";
import { RatingsContentBox } from "@/features/media.ratings/components";
import { Ratings } from "@/features/media.ratings/model";
import HotelClassIcon from "@mui/icons-material/HotelClass";
import { Button, Divider, Pagination, Stack } from "@mui/material";

interface RatingsViewParams {
  id: number;
  ratings: Ratings;
}

export function RatingsSection({ id, ratings }: RatingsViewParams) {
  return (
    <>
      <Section
        title="Ratings"
        additionalButton={<Button startIcon={<HotelClassIcon />}>Rate</Button>}
        className="relative"
      >
        <RatingsContentBox ratings={ratings} />
        <Divider sx={{ marginTop: 2, marginBottom: 2 }} />
        <Stack direction="row" justifyContent="center">
          <Pagination count={10} />
        </Stack>
      </Section>
    </>
  );
}
