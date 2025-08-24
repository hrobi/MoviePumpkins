import { VSpacer } from "@/features/app/components";
import { REM } from "@/features/app/model";
import {
  ReactionButtons,
  SelectedReaction,
} from "@/features/media.reviews/components";
import { Review } from "@/features/media.reviews/model";
import { useRefElementHeight } from "@/utils/hooks";
import { lineBreakIntoParagraph } from "@/utils/lib";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import StarIcon from "@mui/icons-material/Star";
import WarningIcon from "@mui/icons-material/Warning";
import {
  Box,
  Button,
  Collapse,
  IconButton,
  Link as MuiLink,
  Stack,
  Typography,
  useTheme,
} from "@mui/material";
import * as React from "react";

export interface ReviewViewProps {
  review: Review;
}

const MAX_HEIGHT = 10 * REM;

export function ReviewBox({ review }: ReviewViewProps) {
  const theme = useTheme();
  const [selected, setSelected] = React.useState<SelectedReaction>(
    review.userReaction
  );
  const [likes, dislikes] = React.useMemo(
    () => [
      review.reactions.likes + (selected === "like" ? 1 : 0),
      review.reactions.dislikes + (selected === "dislike" ? 1 : 0),
    ],
    [selected]
  );
  const [collapsed, setCollapsed] = React.useState(true);
  const [collapsableContentBoxRef, collapsableContentHeight] =
    useRefElementHeight<HTMLElement>(MAX_HEIGHT);

  console.log("[ReviewBox] height=" + collapsableContentHeight);

  return (
    <Stack direction="column">
      <Stack direction="column">
        <Stack direction="row" justifyContent="space-between">
          <Typography variant="h3">{review.title}</Typography>
          <Stack
            direction="row"
            alignItems="center"
            color={theme.palette.gold.main}
          >
            <StarIcon fontSize="large" />
            <Typography variant="h3" color={theme.palette.gold.dark}>
              {review.rating}
            </Typography>
          </Stack>
        </Stack>
        <Stack direction="row" spacing={1}>
          <span className="font-bold">{review.userDisplayName}</span>
          <MuiLink href={`/@${review.username}`} className="text-sm">
            @{review.username}
          </MuiLink>
        </Stack>
        {!review.spoilerFree && (
          <Typography variant="important">Spoiler</Typography>
        )}
      </Stack>
      <VSpacer size={2} />
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
          <VSpacer size={2} />
          <Stack direction="row">
            <ReactionButtons
              likes={likes}
              dislikes={dislikes}
              selected={selected}
              setSelected={setSelected}
            />
            <Button color="warning" startIcon={<WarningIcon />}>
              Report
            </Button>
          </Stack>
        </Box>
      </Collapse>
      {(collapsableContentHeight > MAX_HEIGHT || !review.spoilerFree) && (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <IconButton onClick={() => setCollapsed(!collapsed)}>
            {collapsed ? <ExpandMoreIcon /> : <ExpandLessIcon />}
          </IconButton>
        </Box>
      )}
    </Stack>
  );
}
