<template>
  {{ role }}
  <UPageHeader>
    <template #title>
      <AppPageTitle>Dune: Part One</AppPageTitle>
    </template>
    <template #links>
        <UNavigationMenu :items="[{ icon: 'i-lucide-pen', label: 'Suggest update', as: 'button' }, { icon: 'i-lucide-star', label: 'Leave your rating' }, { icon: 'i-lucide-notebook-pen', label: 'Add your review' }]" />
    </template>
  </UPageHeader>
  <UPageBody>
    <article class="flex flex-col lg:flex-row gap-5 pt-2">
      <MediaOverview 
        :poster-src="mediaOverview.posterSrc"
        :metadata="mediaOverview.metadata"
        :overall-rating="mediaOverview.overallRating"
        :contributors="mediaOverview.contributors"
      />
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
  import type { MediaOverviewProps } from '~/components/media/overview.vue';

  const { data: role } = await useApi("/users/{username}/role", { path: { username: "marton.teszt" } });

  const useMediaOverview = (): MediaOverviewProps => ({
    posterSrc: "/temp-images/dune-part-1.png",
    overallRating: {
      rating: 8.0,
      voterCount: 1_000_000
    },
    metadata: {
      lengthInMinutes: 155,
      releaseYear: 2021,
      mpaRating: "PG-13",
      awards: 5,
    },
    contributors: {
      directors: ["Denis Villeneuve"],
      writers: ["Jon Spaihts", "Denis Villeneuve", "Eric Roth"],
      stars: ["Timothée Chalamet", "Rebecca Ferguson", "Zendaya"],
    },
  });


  const mediaOverview = useMediaOverview();

</script>

<style>

</style>