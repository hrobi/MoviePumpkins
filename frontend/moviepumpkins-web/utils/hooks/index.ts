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
    setHeight(ref.current!.offsetHeight);
  }, []);
  return [ref, height];
}
