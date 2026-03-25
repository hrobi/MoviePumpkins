<template>
  <div :class="`flex flex-row gap-4 px-4 py-1 w-fit items-center mb-4`">
    <UIcon name="i-lucide-star" :customize="customizingIconFill('var(--color-amber-400)')" mode="svg" class="text-amber-400 text-3xl" />
    <div>
      <h3><span class="text-highlighted text-xl">{{ props.rating.toFixed(1) }}</span>/10</h3>
      <span class="text-sm text-right">{{ formatPeopleCount(props.voterCount) }}</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
  const props = defineProps<{
    rating: number,
    voterCount: number
  }>();
</script>

<style>

</style>