"use client";

import { Ref, useCallback, useEffect, useRef, useState } from "react";

export function useToggle(): [boolean, () => void] {
  const [b, setB] = useState<boolean>(false);
  const toggle = useCallback(() => setB(!b), [b]);
  return [b, toggle];
}

export function useRefElementWidth<E extends HTMLElement>(
  baseWidth: number
): [Ref<E> | undefined, number] {
  const ref = useRef<E>(undefined);
  const [width, setWidth] = useState(baseWidth);
  useEffect(() => {
    if (!ref.current) {
      return;
    }
    const { width } = ref.current.getBoundingClientRect();
    setWidth(width);
  }, []);
  return [ref as Ref<E>, width];
}
