import type { NavigationMenuItem } from "@nuxt/ui";
import type { UserRole } from "~~/shared/types/user";

export const useNavigationItems = () => {
  const { user, loggedIn } = useUserSession();
  const route = useRoute();

  const role = computed<UserRole | undefined>(() =>
    loggedIn.value ? user.value?.role : undefined
  );

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

    watchEffect(() => { console.log(role.value) })

    switch(role.value) {
      case "REVIEWER":
        return [watchlist, notification];
      case "ADMIN":
        return [watchlist, tasks];
      case "SUPERVISOR":
        return [watchlist, tasks];
      case undefined:
        return [watchlist];
      default:
        assertNever(role.value);
    }
  });

  const showLoginButton = computed(() => !loggedIn.value);
  const showLogoutButton = computed(() => loggedIn.value);

  return { navigationItems, showLoginButton, showLogoutButton };
}
