<template>
  <UCard variant="subtle">
    <template #header>
      {{ props.flavor }}
    </template>
    <div class="flex flex-row items-center gap-2">
      <PercentageSlide class="grow" :value="props.percentage" :color="color" />
      <span>{{ props.percentage }}%</span>
    </div>
  </UCard>
</template>

<script lang="ts" setup>
import type { Color } from '~/components/percentage-slide.vue';

  export interface FlavorRatingProps {
      flavor: string;
      percentage: number;
      color?: Color | undefined;
  }

  const props = defineProps<FlavorRatingProps>();
  const color = props.color || "orange";

</script>

<style>

</style>