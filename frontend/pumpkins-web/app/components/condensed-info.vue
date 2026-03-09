<template>
  <div :class="`flex gap-2 ${
    direction == 'horizontal'
    ? responsive
    ? 'flex-col w-full sm:flex-row sm:w-fit' 
    : 'flex-row w-fit'
    : 'flex-col w-full'
    }
    ${props.class ?? ''}`">
    <div v-for="item in props.items" class="flex flex-row items-center gap-4 px-4 py-2 rounded-md bg-elevated/50">
      <UIcon v-if="item.icon" :name="item.icon" class="text-primary text-2xl" />
      <span v-if="!item.title">{{ item.content }}</span>
      <div v-if="!!item.title">
        <h3 class="text-default/70">{{ item.title }}</h3>
        <span>{{ item.content }}</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  export interface CondensedInfoItem {
    icon?: string,
    title?: string,
    content: string,
  }

  const props = defineProps<{
    items: CondensedInfoItem[],
    direction?: "horizontal" | "vertical" | undefined,
    class?: string | undefined,
    responsive?: boolean | undefined
  }>();

  const direction = props.direction || "horizontal";
  const responsive = props.responsive || false;
</script>

<style>

</style>