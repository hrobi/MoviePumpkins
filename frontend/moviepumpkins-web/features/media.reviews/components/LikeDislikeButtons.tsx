"use client";

import ThumbDownIcon from "@mui/icons-material/ThumbDown";
import ThumbUpIcon from "@mui/icons-material/ThumbUp";
import { alpha, styled, Theme, ToggleButton, ToggleButtonGroup } from "@mui/material";
import { Dispatch, SetStateAction } from "react";

export type SelectableReaction = "like" | "dislike";
export type SelectedReaction = SelectableReaction | undefined;

interface ReactionButtonsParams {
    likes: number;
    dislikes: number;
    selected: SelectedReaction;
    setSelected: Dispatch<SetStateAction<SelectedReaction>>;
}

const ReactionToggleButton = styled(ToggleButton)(
    ({theme, value}: { theme?: Theme; value: SelectableReaction }) => ({
        border: "none",
        display: "flex",
        gap: theme!.spacing(1),
        color: value === "like" ? theme?.palette.like : theme?.palette.dislike,
        "&:hover": {
            backgroundColor:
                value === "like"
                    ? alpha(theme!.palette.like, 0.1)
                    : alpha(theme!.palette.dislike, 0.1),
        },
        "&.Mui-selected": {
            color: value === "like" ? theme?.palette.like : theme?.palette.dislike,
            backgroundColor:
                value === "like"
                    ? alpha(theme!.palette.like, 0.1)
                    : alpha(theme!.palette.dislike, 0.1),
            "&:hover": {
                backgroundColor:
                    value === "like"
                        ? alpha(theme!.palette.like, 0.2)
                        : alpha(theme!.palette.dislike, 0.2),
            },
        },
    }),
);

export function ReactionButtons({
                                    likes,
                                    dislikes,
                                    selected,
                                    setSelected,
                                }: ReactionButtonsParams) {
    return (
        <ToggleButtonGroup
            value={selected}
            exclusive
            onChange={(event, newReaction) => {
                setSelected(newReaction);
            }}
        >
            <ReactionToggleButton name="like" type="submit" value="like">
                <ThumbUpIcon/>
                {likes}
            </ReactionToggleButton>
            <ReactionToggleButton
                name="dislike"
                type="submit"
                sx={{border: "none"}}
                value="dislike"
            >
                <ThumbDownIcon/>
                {dislikes}
            </ReactionToggleButton>
        </ToggleButtonGroup>
    );
}
