"use client";

import { Rating } from "@/features/media.ratings/model/Ratings";
import { Card, CardContent, Rating as RatingComponent, Stack, Typography, useTheme } from "@mui/material";

interface RatingCardParams {
    rating: Rating;
    gold?: boolean;
}

export function RatingCard({rating, gold = false}: RatingCardParams) {
    const theme = useTheme();

    return (
        <>
            <Card
                variant="outlined"
                sx={{
                    ...(gold
                        ? {
                            backgroundColor: theme.palette.gold.light,
                            color: theme.palette.gold.dark,
                            borderColor: theme.palette.gold.main,
                        }
                        : {}),
                }}
            >
                <CardContent>
                    <Stack direction="row" justifyContent="space-between">
                        <Typography
                            variant="h3"
                            sx={{
                                fontSize: "1.4rem",
                            }}
                        >
                            {rating.flavour.name}
                        </Typography>
                        <Typography variant="h3" sx={{fontSize: "1.4rem"}}>
                            {rating.score}
                        </Typography>
                    </Stack>
                </CardContent>
                <CardContent
                    sx={{
                        paddingTop: 0,
                        paddingBottom: 0,
                    }}
                >
                    <RatingComponent
                        value={rating.score}
                        sx={
                            gold
                                ? {
                                    "& span svg": {
                                        fill: theme.palette.gold.main,
                                    },
                                }
                                : {}
                        }
                    />
                </CardContent>
                <CardContent sx={{paddingTop: 0, paddingBottom: 0}}>
                    by ratings of {rating.count} people
                </CardContent>
            </Card>
        </>
    );
}
