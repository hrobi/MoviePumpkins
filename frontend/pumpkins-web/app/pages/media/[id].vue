<template>
  <UPageHeader>
    <template #title>
      {{ mediaData?.title }}
    </template>

    <template #links>
      <UNavigationMenu
        :items="[
          { icon: 'i-lucide-pen', label: 'Suggest update', as: 'button' },
          { icon: 'i-lucide-star', label: 'Leave your rating' },
          { icon: 'i-lucide-notebook-pen', label: 'Add your review' },
        ]"
      />
    </template>
  </UPageHeader>
  <UPageBody>
    <article class="flex flex-col lg:flex-row gap-5 pt-2">
      <MediaOverview
        v-if="mediaData"
        :media-data="mediaData"
        :overall-rating="{ rating: 8, voterCount: 100_000 }"
      />
    </article>
    <USeparator class="w-1/2 mx-auto" />
    <article>
      <UPageGrid>
        <MediaRatingCard
          flavor="Visuals"
          :percentage="97"
          color="orange"
        />
        <MediaRatingCard
          flavor="Story"
          :percentage="90"
          color="red"
        />
        <MediaRatingCard
          flavor="Epic"
          :percentage="89"
          color="purple"
        />
        <MediaRatingCard
          flavor="Action"
          :percentage="80"
          color="blue"
        />
        <MediaRatingCard
          flavor="Characters"
          :percentage="78"
          color="sky"
        />
        <MediaRatingCard
          flavor="Something"
          :percentage="30"
          color="amber"
        />
      </UPageGrid>
      <UPagination
        class="my-10 flex flex-row justify-center"
        variant="ghost"
        :total="90"
      />
    </article>
    <USeparator class="w-1/2 mx-auto" />
    <UCollapsible
      v-if="loggedIn"
      :unmount-on-hide="false"
    >
      <div class="flex flex-row justify-end">
        <UButton
          label="Leave review"
          color="neutral"
          variant="subtle"
          icon="i-lucide-notebook-pen"
        />
      </div>

      <template #content>
        <MediaOwnReview
          v-model="ownReview.form"
          :data="ownReview.data"
          :submit="ownReview.submit"
          :submitting="ownReview.submitting"
        />
      </template>
    </UCollapsible>
    <MediaReviews
      v-model:page="reviews.page.value"
      :data="reviews.data.value"
      :paging="reviews.paging.value"
      :status="reviews.status.value"
    />
  </UPageBody>
</template>

<script lang="ts" setup>
import type { MediaData } from "#shared/types/media";

const { loggedIn } = useUserSession();

const route = useRoute();
const mediaId = Number(route.params.id as string);

const useApiMedia = async ({ mediaId }: { mediaId: number }) => {
  return await useApi("/media/{id}", { path: { id: mediaId } });
};

const { error, data } = await useApiMedia({ mediaId });

if (error.value) {
  throw createError(error.value);
}

const mediaData: ComputedRef<MediaData | undefined> = computed(() => {
  return data.value;
});

const reviews = await useReviews({ mediaId });

const ownReview = await useOwnReview({ mediaId });
</script>

<style></style>
