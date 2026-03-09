<template>
  <UPageHeader>
    <template #title>
      <h1 class="text-3xl sm:text-4xl text-pretty font-bold text-highlighted">Dune: Part One</h1>
    </template>
    <template #links>
        <UNavigationMenu :items="[{ icon: 'i-lucide-pen', label: 'Suggest update', as: 'button' }, { icon: 'i-lucide-star', label: 'Leave your rating' }, { icon: 'i-lucide-notebook-pen', label: 'Add your review' }]" />
    </template>
  </UPageHeader>
  <UPageBody>
    <article class="flex flex-col lg:flex-row gap-5 pt-2">
      <div class="flex flex-row justify-center">
        <img src="/temp-images/dune-part-1.png" class="rounded-md" width="400" />
      </div>
      <div>
        <div class="flex flex-col justify-start items-center lg:flex-row lg:items-baseline gap-5">
          <MediaOverallRating />
          <CondensedInfo :responsive="true" class="mb-5" :items="shortCondensedInfoItems" />
        </div>
        <p>Paul Atreides arrives on Arrakis after his father accepts the stewardship of the dangerous planet. However, chaos ensues after a betrayal as forces clash to control melange, a precious resource.</p>
        <CondensedInfo class="mt-5" direction="vertical" :items="longCondensedInfoItems" />
      </div>
    </article>
    <USeparator class="w-1/2 mx-auto" />
    <article>
      <UPageGrid>
        <MediaRatingCard flavor="Visuals" :percentage="97" color="orange" />
        <MediaRatingCard flavor="Story" :percentage="90" color="red" />
        <MediaRatingCard flavor="Epic" :percentage="89" color="purple" />
        <MediaRatingCard flavor="Action" :percentage="80" color="blue" />
        <MediaRatingCard flavor="Characters" :percentage="78" color="sky" />
        <MediaRatingCard flavor="Something" :percentage="30" color="amber" />
      </UPageGrid>
      <UPagination class="my-10 flex flex-row justify-center" variant="ghost" :total="90" />
    </article>
    <USeparator class="w-1/2 mx-auto" />
    <article>
      <UCard v-for="_ in Array(5)" variant="subtle" class="mb-5">
        <template #header>
          <h2 class="text-xl">Everything i wanted and more</h2>
        </template>
        <p>This movie is not only beautiful in every aspect its also unique in the way its filmed whitch makes it unbelievably good.</p>
      </UCard>
      <UPagination class="my-10 flex flex-row justify-center" variant="ghost" :total="90" />
    </article>
  </UPageBody>
</template>

<script lang="ts" setup>
  import type { CondensedInfoItem } from '~/components/condensed-info.vue';

  const useMediaDetails = () => ({
    title: "Dune: Part I",
    overallRating: 8.0,
    trivia: {
      lengthInMinutes: 155,
      releaseYear: 2021,
      mpaRating: "PG-13",
      awards: 5,
      directors: ["Denis Villeneuve"],
      writers: ["Jon Spaihts", "Denis Villeneuve", "Eric Roth"],
      stars: ["Timothée Chalamet", "Rebecca Ferguson", "Zendaya"],
    }
  });

  const { title, trivia, overallRating } = useMediaDetails();

  const shortCondensedInfoItems: CondensedInfoItem[] = [
    {
      icon: "i-lucide-hourglass",
      content: formatMinutes(trivia.lengthInMinutes)
    },
    {
      icon: "i-lucide-calendar-1",
      content: trivia.releaseYear.toString()
    },
    {
      icon: "i-lucide-shield-alert",
      content: trivia.mpaRating
    },
    {
      icon: "i-lucide-award",
      content: trivia.awards.toString()
    }
  ];

  const longCondensedInfoItems: CondensedInfoItem[] = [
    {
      icon: "i-lucide-ship-wheel",
      title: pluralize("Director", trivia.directors.length),
      content: trivia.directors.join(", ")
    },
    {
      icon: "i-lucide-pen",
      title: pluralize("Writer", trivia.writers.length),
      content: trivia.writers.join(", ")
    },
    {
      icon: "i-lucide-star",
      title: pluralize("Star", trivia.stars.length),
      content: trivia.stars.join(", ")
    },
  ];

</script>

<style>

</style>