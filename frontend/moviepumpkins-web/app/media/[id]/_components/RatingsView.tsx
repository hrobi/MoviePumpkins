"use client";

import Section from "@/components/Section";
import { drop, take } from "@/lib";
import { Ratings } from "@/model/Ratings";
import HotelClassIcon from "@mui/icons-material/HotelClass";
import { Button, Divider, Pagination, Stack } from "@mui/material";
import { useRef } from "react";
import { RatingsContentBox } from "../../../../components/rating/RatingsContentBox";

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
