"use client";

import { VSpacer } from "@/features/app/components";
import { REM } from "@/features/app/model";
import { Review } from "@/features/media.reviews/model";
import { useRefElementHeight } from "@/utils/hooks";
import { lineBreakIntoParagraph } from "@/utils/lib";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import WarningIcon from "@mui/icons-material/Warning";
import { Box, Button, Collapse, IconButton, Link as MuiLink, Stack, Typography } from "@mui/material";
import * as React from "react";

export interface ReviewViewProps {
    review: Review;
}

const MAX_HEIGHT = 10 * REM;

export function ReviewBox({review}: ReviewViewProps) {
    const [collapsed, setCollapsed] = React.useState(true);
    const [collapsableContentBoxRef, collapsableContentHeight] =
        useRefElementHeight<HTMLElement>(MAX_HEIGHT);

    console.log("[ReviewBox] height=" + collapsableContentHeight);

    return (
        <Stack direction="column">
            <Stack direction="column">
                <Stack direction="row" justifyContent="space-between">
                    <Typography variant="h3">{review.title}</Typography>
                </Stack>
                <Stack direction="row" spacing={1}>
                    <span className="font-bold">{review.user.displayName}</span>
                    <MuiLink href={`/@${review.user.username}`} className="text-sm">
                        @{review.user.username}
                    </MuiLink>
                </Stack>
                {!review.spoilerFree && <Typography variant="important">Spoiler</Typography>}
            </Stack>
            <VSpacer size={2}/>
            <Collapse
                in={!collapsed}
                collapsedSize={
                    !review.spoilerFree
                        ? 0
                        : collapsableContentHeight < MAX_HEIGHT
                            ? collapsableContentHeight
                            : MAX_HEIGHT
                }
            >
                <Box ref={collapsableContentBoxRef}>
                    {lineBreakIntoParagraph(review.content)}
                    <VSpacer size={2}/>
                    <Stack direction="row">
                        {/* <ReviewReactions review={review} /> */}
                        <Button color="warning" startIcon={<WarningIcon/>}>
                            Report
                        </Button>
                    </Stack>
                </Box>
            </Collapse>
            {(collapsableContentHeight > MAX_HEIGHT || !review.spoilerFree) && (
                <Box sx={{display: "flex", justifyContent: "center"}}>
                    <IconButton onClick={() => setCollapsed(!collapsed)}>
                        {collapsed ? <ExpandMoreIcon/> : <ExpandLessIcon/>}
                    </IconButton>
                </Box>
            )}
        </Stack>
    );
}
