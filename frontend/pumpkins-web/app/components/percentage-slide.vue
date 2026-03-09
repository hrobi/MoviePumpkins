<template>
  <div class="relative h-2">
    <div class="absolute w-full h-2 rounded-full bg-elevated"></div>
    <div class="absolute" :style="{ width: `${props.value}%` }">
      <Transition>
        <div v-if="show" :class="`h-2 rounded-full w-full ${props.color}`"></div>
      </Transition>
    </div>
  </div>
</template>

<script lang="ts" setup>
  export type Color = "orange" | "red" | "blue" | "sky" | "purple" | "amber";

  const usePercentage = () => {
    const show = ref(false);
    
    onMounted(() => {
      show.value = true;
    });
  
    return { show };
  }

  const props = defineProps<{
    value: number,
    color: Color,
  }>();

  const { show } = usePercentage();
</script>

<style>
  .v-enter-active {
    transition: width 0.5s ease;
  }

  .v-enter-from {
    width: 0;
  }

  .orange {
    background-color: var(--ui-color-orange);
  }

  .red {
    background-color: var(--ui-color-red);
  }

  .blue {
    background-color: var(--ui-color-blue);
  }

  .sky {
    background-color: var(--ui-color-sky);
  }

  .purple {
    background-color: var(--ui-color-purple);
  }

  .amber {
    background-color: var(--ui-color-amber);
  }
</style>