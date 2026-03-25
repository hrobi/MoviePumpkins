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
      <UButton v-if="showLoginButton" class="cursor-pointer" variant="solid" @click="navigateTo('/api/auth/callback/keycloak', { external: true })">Login</UButton>
      <UButton v-if="showLogoutButton" class="cursor-pointer bg-logout hover:bg-logout/70" variant="solid" @click="navigateTo('/auth/logout', { external: true })">Logout</UButton>
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