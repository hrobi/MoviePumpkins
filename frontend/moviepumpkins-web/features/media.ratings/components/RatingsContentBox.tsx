"use client";

import { RatingCard } from "@/features/media.ratings/components";
import { Rating } from "@/features/media.ratings/model";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { Box, Collapse, Grid, IconButton } from "@mui/material";
import { useState } from "react";

const ROW_HEIGHT = 175;

export function RatingsContentBox({ratings}: { ratings: Rating[] }) {
    const [collapsed, setCollapsed] = useState(true);
    return (
        <>
            <Collapse in={!collapsed} collapsedSize={ROW_HEIGHT * (ratings.length / 4)}>
                <Grid container spacing={2}>
                    {ratings.map((rating) => (
                        <Grid key={rating.flavour.id} size={{lg: 3, md: 4, xs: 12}}>
                            <RatingCard rating={rating} gold={rating.flavour.id === "OVRL"}/>
                        </Grid>
                    ))}
                </Grid>
            </Collapse>
            <Box sx={{justifyContent: "center", display: {xs: "flex", lg: "none"}}}>
                <IconButton onClick={() => setCollapsed(!collapsed)}>
                    {collapsed ? <ExpandMoreIcon/> : <ExpandLessIcon/>}
                </IconButton>
            </Box>
        </>
    );
}
