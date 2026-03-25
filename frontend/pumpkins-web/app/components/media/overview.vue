<template>
  <div class="flex flex-row justify-center">
    <img :src="props.posterSrc" class="rounded-md" width="400" />
  </div>
  <div>
    <div class="flex flex-col justify-start items-center lg:flex-row lg:items-baseline gap-5">
      <MediaOverallRating :rating="props.overallRating.rating" :voter-count="props.overallRating.voterCount" />
      <CondensedInfo :responsive="true" class="mb-5" :items="metadataItems" />
    </div>
    <p>Paul Atreides arrives on Arrakis after his father accepts the stewardship of the dangerous planet. However, chaos ensues after a betrayal as forces clash to control melange, a precious resource.</p>
    <CondensedInfo class="mt-5" direction="vertical" :items="contributorItems" />
  </div>
</template>

<script lang="ts" setup>
  import type { CondensedInfoItem } from '../condensed-info.vue';

  export interface MediaOverviewProps {
    posterSrc: string,
    overallRating: {
      rating: number,
      voterCount: number,
    },
    metadata: {
      lengthInMinutes: number,
      releaseYear: number,
      mpaRating: string,
      awards: number,
    },
    contributors: {
      directors: string[],
      writers: string[],
      stars: string[]
    }
  }

  const props = defineProps<MediaOverviewProps>();

  const metadataItems: CondensedInfoItem[] = [
    {
      icon: "i-lucide-hourglass",
      content: formatMinutes(props.metadata.lengthInMinutes)
    },
    {
      icon: "i-lucide-calendar-1",
      content: props.metadata.releaseYear.toString()
    },
    {
      icon: "i-lucide-shield-alert",
      content: props.metadata.mpaRating
    },
    {
      icon: "i-lucide-award",
      content: props.metadata.awards.toString()
    }
  ];

  const contributorItems: CondensedInfoItem[] = [
    {
      icon: "i-lucide-ship-wheel",
      title: pluralize("Director", props.contributors.directors.length),
      content: props.contributors.directors.join(", ")
    },
    {
      icon: "i-lucide-pen",
      title: pluralize("Writer", props.contributors.writers.length),
      content: props.contributors.writers.join(", ")
    },
    {
      icon: "i-lucide-star",
      title: pluralize("Star", props.contributors.stars.length),
      content: props.contributors.stars.join(", ")
    },
  ];
</script>

<style>

</style>