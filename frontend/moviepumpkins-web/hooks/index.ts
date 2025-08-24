"use client";

import { Ref, useCallback, useEffect, useRef, useState } from "react";

export function useToggle(): [boolean, () => void] {
  const [b, setB] = useState<boolean>(false);
  const toggle = useCallback(() => setB(!b), [b]);
  return [b, toggle];
}

export function useRefElementHeight<E extends HTMLElement>(
  defaultHeight: number
): [Ref<E> | undefined, number] {
  const ref = useRef<E>(null);
  const [height, setHeight] = useState(defaultHeight);
  useEffect(() => {
    console.log("[useMaxRefElementHeight] " + ref.current!.offsetHeight);
    setHeight(ref.current!.offsetHeight);
  }, []);
  return [ref, height];
}

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
