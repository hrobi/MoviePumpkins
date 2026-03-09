<template>
  <UHeader :ui="{ right: 'lg:flex-0', left: 'flex-1' }">
    <template #left>
      <h2 class="text-center text-primary">Movie Pumpkins</h2>
      <UFieldGroup :ui="{ base: 'w-full' }">
        <UInput class="flex-1" placeholder="Search for media title..." />
        <UButton icon="i-lucide-search"></UButton>
      </UFieldGroup>
    </template>
    <UNavigationMenu :items="navigationItems" />
    <template #right>
      <UButton v-if="showLoginButton" class="cursor-pointer" variant="solid">Login</UButton>
      <UButton v-if="showLogoutButton" class="cursor-pointer bg-logout hover:bg-logout/70" variant="solid">Logout</UButton>
      <UColorModeButton />
    </template>
    <template #body>
      <UNavigationMenu 
        :items="navigationItems"
       />
    </template>
  </UHeader>
  <slot />
</template>

<script lang="ts" setup>
  const { navigationItems, showLoginButton, showLogoutButton } = toRefs(useNavigationItems());
</script>

<style>

</style>