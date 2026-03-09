import type { NavigationMenuItem } from "@nuxt/ui";
import type { UserRole } from "~/types/user";

export const useNavigationItems = () => {
  const role = ref(defaultUserRole());
  const route = useRoute();

  const navigationItems = computed<NavigationMenuItem[]>(() => {

    const watchlist = {
      label: "Watchlist",
      icon: "i-lucide-eye",
      to: "/watchlist",
      active: route.path.startsWith("/watchlist")
    };

    const tasks = {
      label: "Tasks",
      icon: "i-lucide-square-check-big",
      to: "/tasks",
      active: route.path.startsWith("/tasks"),
      chip: { text: 5, size: "lg" }
    };

    const notification = {
      label: "Notifications",
      icon: "i-lucide-bell",
      to: "/notifications",
      active: route.path.startsWith("/notifications")
    };

    switch(role.value) {
      case "reviewer":
        return [watchlist, notification];
      case "admin":
        return [watchlist, tasks];
      case "supervisor":
        return [watchlist, tasks];
      case undefined:
        return [watchlist];
      default:
        assertNever(role.value);
    }
  });

  const showLoginButton = computed(() => role.value == undefined);
  const showLogoutButton = computed(() => role.value != undefined);

  return { navigationItems, showLoginButton, showLogoutButton };
}
