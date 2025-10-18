"use client";

import { Section } from "@/features/app.scaffold/components";
import { MediaDetails } from "@/features/media/model";
import { lineBreakIntoParagraph } from "@/utils/lib";
import AddIcon from "@mui/icons-material/Create";
import { Badge, Box, Button, Chip, Divider, Grid, Stack, Typography } from "@mui/material";
import { getAwardTupleFromAwardString } from "../lib/StringFormattingForMedia";

interface MediaDetailsViewParams {
    details: MediaDetails;
}

export function ChipStack({label, values}: { label: string; values: string[] }) {
    return (
        <Stack direction="row" spacing={2}>
            <Chip variant="accent" label={label}/>
            <Stack direction="row" spacing={1}>
                {values.map((value, index) => (
                    <Chip key={index} variant="outlined" label={value}/>
                ))}
            </Stack>
        </Stack>
    );
}

export function MediaDetailsSection({details}: MediaDetailsViewParams) {
    const {
        posterLink,
        title,
        description,
        directors,
        actors,
        writers,
        awards,
        totalWins,
        totalNominations,
        originalTitle,
    } = details;

    const awardTuples = awards?.map((awardString) =>
        getAwardTupleFromAwardString(awardString),
    );

    return (
        <>
            <Section
                title={title}
                additionalButton={<Button startIcon={<AddIcon/>}>Modify</Button>}
                className="relative"
            >
                <Grid container spacing={2}>
                    <Grid size={3}>
                        <img className="rounded-md" src={posterLink}/>
                    </Grid>
                    <Grid size={9}>
                        <Stack direction="row" spacing={1} alignItems="center">
                            {originalTitle && (
                                <>
                                    <Chip label="original title"/>
                                    <Typography variant="body">{originalTitle}</Typography>
                                </>
                            )}
                            {details.type == "movie" && (
                                <>
                                    <Chip label="year"/>
                                    <Typography variant="body">{details.releaseYear}</Typography>
                                </>
                            )}
                            {details.type === "series" && (
                                <>
                                    <Chip label="start year - end year"/>
                                    <Typography variant="body">
                                        {details.startedInYear} - {details.endedInYear ?? ""}
                                    </Typography>
                                </>
                            )}
                        </Stack>
                        <Divider sx={{marginTop: 2, marginBottom: 2}}/>
                        <Box>{lineBreakIntoParagraph(description)}</Box>
                        <Divider sx={{marginTop: 2, marginBottom: 2}}/>
                        <Stack spacing={2}>
                            {directors && directors.length > 0 && (
                                <ChipStack label="Directors" values={directors}/>
                            )}
                            {actors && actors.length > 0 && (
                                <ChipStack label="Actors" values={actors}/>
                            )}
                            {writers && writers.length > 0 && (
                                <ChipStack label="Writers" values={writers}/>
                            )}
                            {(awards || totalNominations || totalWins) && (
                                <Stack direction="row" spacing={2}>
                                    <Chip variant="accent" label="Awards"/>
                                    <Stack direction="row" spacing={2}>
                                        {totalWins && (
                                            <Badge badgeContent={totalWins} color="info">
                                                <Chip label="Wins" color="gold"/>
                                            </Badge>
                                        )}
                                        {totalNominations && (
                                            <Badge badgeContent={totalNominations} color="info">
                                                <Chip label="Nominations" color="info"/>
                                            </Badge>
                                        )}
                                        {awardTuples?.map(([count, name], index) => (
                                            <Badge key={index} badgeContent={count} color="info">
                                                <Chip label={name}/>
                                            </Badge>
                                        ))}
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
