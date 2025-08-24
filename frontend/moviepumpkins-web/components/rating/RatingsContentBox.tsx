import { Rating } from "@/model/Ratings";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { Box, Collapse, Grid, IconButton } from "@mui/material";
import { useState } from "react";
import { RatingCard } from "./RatingCard";

const ROW_HEIGHT = 175;

export function RatingsContentBox({ ratings }: { ratings: Rating[] }) {
  const [collapsed, setCollapsed] = useState(true);
  return (
    <>
      <Collapse in={!collapsed} collapsedSize={ROW_HEIGHT * 3}>
        <Grid container spacing={2}>
          {ratings.map((rating, index) => (
            <Grid key={index} size={{ lg: 3, md: 4, xs: 12 }}>
              <RatingCard rating={ratings[index]} gold={index == 0} />
            </Grid>
          ))}
        </Grid>
      </Collapse>
      <Box
        sx={{ justifyContent: "center", display: { xs: "flex", lg: "none" } }}
      >
        <IconButton onClick={() => setCollapsed(!collapsed)}>
          {collapsed ? <ExpandMoreIcon /> : <ExpandLessIcon />}
        </IconButton>
      </Box>
    </>
  );
}
