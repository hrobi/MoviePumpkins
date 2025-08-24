"use client";

import Section from "@/components/Section";
import { lineBreakIntoParagraph } from "@/lib";
import { MediaDetails } from "@/model/MediaDetails";
import AddIcon from "@mui/icons-material/Create";
import { Badge, Box, Button, Chip, Divider, Grid, Stack } from "@mui/material";

interface MediaDetailsViewParams {
  details: MediaDetails;
}

export function ChipStack({
  label,
  values,
}: {
  label: string;
  values: string[];
}) {
  return (
    <Stack direction="row" spacing={2}>
      <Chip variant="accent" label={label} />
      <Stack direction="row" spacing={1}>
        {values.map((value, index) => (
          <Chip key={index} variant="outlined" label={value} />
        ))}
      </Stack>
    </Stack>
  );
}

export default function MediaDetailsView({ details }: MediaDetailsViewParams) {
  return (
    <>
      <Section
        title={details.title}
        additionalButton={<Button startIcon={<AddIcon />}>Modify</Button>}
        className="relative"
      >
        <Grid container spacing={2}>
          <Grid size={3}>
            <img className="rounded-md" src={details.pictureHref} />
          </Grid>
          <Grid size={9}>
            <Box>{lineBreakIntoParagraph(details.shortDescription)}</Box>
            <Divider sx={{ marginTop: 2, marginBottom: 2 }} />
            <Stack spacing={2}>
              {details.directors.length > 0 && (
                <ChipStack label="Directors" values={details.directors} />
              )}
              {details.actors.length > 0 && (
                <ChipStack label="Actors" values={details.actors} />
              )}
              {details.writers.length > 0 && (
                <ChipStack label="Writers" values={details.writers} />
              )}
              {details.awards && (
                <Stack direction="row" spacing={2}>
                  <Chip variant="accent" label="Awards" />
                  <Stack direction="row" spacing={2}>
                    <Badge
                      badgeContent={details.awards.totalWinCount}
                      color="info"
                    >
                      <Chip label="Wins" color="gold" />
                    </Badge>
                    <Badge
                      badgeContent={details.awards.totalNominationCount}
                      color="info"
                    >
                      <Chip label="Nominations" color="info" />
                    </Badge>
                    {details.awards.namedAwards.map(
                      ({ count, type }, index) => (
                        <Badge key={index} badgeContent={count} color="info">
                          <Chip label={type} />
                        </Badge>
                      )
                    )}
                  </Stack>
                </Stack>
              )}
            </Stack>
          </Grid>
        </Grid>
      </Section>
    </>
  );
}
