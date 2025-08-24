import { useState } from "react";

export function useMenuAnchor<E extends HTMLElement>(): {
  anchorEl: HTMLElement | null;
  open: boolean;
  openOnEvent: (event: React.MouseEvent<E>) => void;
  onClose: () => void;
} {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const openOnEvent = (event: React.MouseEvent<E>) => {
    setAnchorEl(event.currentTarget);
  };
  const onClose = () => {
    setAnchorEl(null);
  };

  return { anchorEl, open, openOnEvent, onClose };
}
