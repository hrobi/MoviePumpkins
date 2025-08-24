"use client";

import { Skeleton, Stack } from "@mui/material";

function randomLengthsArray() {
  return Array<number>(12)
    .fill(1)
    .map((value) => Math.random() * (300 - 50) + 50);
}

export function RandomTextSkeleton() {
  const randomLengths = randomLengthsArray();
  console.log(randomLengths);
  return (
    <Stack direction="row" flexWrap="wrap" spacing={2}>
      {randomLengths.map((length, index) => (
        <Skeleton variant="text" key={index} width={length} />
      ))}
    </Stack>
  );
}
