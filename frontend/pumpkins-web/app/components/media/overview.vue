<template>
  <div class="flex flex-row flex-1 justify-center items-start">
    <img
      v-if="props.mediaData.posterUrl"
      :src="props.mediaData.posterUrl"
      class="rounded-md w-full"
    />
  </div>
  <div class="flex-2">
    <h2 class="text-4xl text-highlighted/60 mb-2">Plot</h2>
    <p class="text-md text-pretty font-bold text-highlighted pb-5">
      {{ props.mediaData.plot ?? "?" }}
    </p>
    <CondensedInfo :responsive="true" :items="headerConsdensedInfo" />
    <CondensedInfo
      class="mt-5"
      direction="vertical"
      :items="footerCondensedInfo"
    />
  </div>
</template>

<script lang="ts" setup>
import { Array, Match, Option } from "effect";
import type { MediaData } from "~~/shared/types/media";
import type { CondensedInfoItem } from "../condensed-info.vue";

export interface MediaOverviewProps {
  overallRating: {
    rating: number;
    voterCount: number;
  };
  mediaData: MediaData;
}

const props = defineProps<MediaOverviewProps>();

const awardWinCount = props.mediaData.awardWinCount;
const awardNominationCount = props.mediaData.awardNominationCount;

const headerConsdensedInfo: CondensedInfoItem[] = [
  {
    icon: "i-lucide-calendar-1",
    content: Match.value(props.mediaData).pipe(
      Match.tagsExhaustive({
        FeatureFilm: ({ releaseYear }) =>
          releaseYear ? `${releaseYear}` : "?",
        Series: ({ releaseYear, lastYear }) => {
          if (!releaseYear) {
            return "?";
          }

          return `${releaseYear} - ${lastYear ?? "?"}`;
        },
      }),
    ),
  },
  Match.value(props.mediaData).pipe(
    Match.withReturnType<CondensedInfoItem>(),
    Match.tagsExhaustive({
      FeatureFilm: ({ runtimeInMinutes }) => ({
        icon: "i-lucide-clock",
        content: runtimeInMinutes ? `${runtimeInMinutes} min` : "? min",
      }),
      Series: ({ numberOfSeasons }) => ({
        icon: "i-lucide-tally-5",
        content: numberOfSeasons ? `${numberOfSeasons}` : "?",
      }),
    }),
  ),
  {
    icon: "i-lucide-flag",
    content: props.mediaData.mpaRating ?? "?",
  },
  ...Array.fromNullable(
    awardWinCount
      ? awardWinCount === 0
        ? undefined
        : { icon: "i-lucide-award", content: `${awardWinCount}` }
      : { icon: "i-lucide-award", content: "?" },
  ),
  ...Array.fromNullable(
    awardNominationCount
      ? awardNominationCount === 0
        ? undefined
        : { icon: "i-lucide-hand-heart", content: `${awardNominationCount}` }
      : { icon: "i-lucide-hand-heart", content: "?" },
  ),
];

const footerCondensedInfo: CondensedInfoItem[] = [
  {
    title: "Genres",
    content: props.mediaData.genres?.join(", ") ?? "?",
  },
  {
    title: "Director(s)",
    content: props.mediaData.directors?.join(", ") ?? "?",
  },
  {
    title: "Writer(s)",
    content: props.mediaData.writers?.join(", ") ?? "?",
  },
  {
    title: "Actor(s)",
    content: props.mediaData.actors?.join(", ") ?? "?",
  },
];
</script>

<style></style>
