"use client";

import React, { useEffect, useMemo, useRef, useState } from "react";
import ShowMoreButton from "./ShowMoreButton";

export interface ExpandableBoxParams {
  baseHeight: number;
  overShadowed?: boolean;
  children: React.ReactNode;
}

export default function ExpandableBox({
  baseHeight,
  children,
}: ExpandableBoxParams) {
  const [isExpanded, setExpanded] = useState(false);
  const [contentBaseHeight, setContentBaseHeight] = useState(baseHeight);
  const contentHeight = useMemo(
    () =>
      contentBaseHeight <= baseHeight
        ? contentBaseHeight
        : isExpanded
        ? contentBaseHeight
        : baseHeight,
    [isExpanded, contentBaseHeight]
  );
  const containerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!containerRef.current) {
      return;
    }
    const { height } = containerRef.current?.getBoundingClientRect();
    setContentBaseHeight(height);
  }, []);

  return (
    <>
      <div
        className="overflow-hidden relative"
        style={{ height: contentHeight, transition: "height 500ms" }}
      >
        <div ref={containerRef}>{children}</div>
        {contentBaseHeight > baseHeight && !isExpanded && (
          <div
            className="w-full h-[3rem] bottom-0 absolute"
            style={{
              background:
                "linear-gradient(0deg,rgba(255, 255, 255, 1) 0%, rgba(255, 255, 255, 0) 100%)",
            }}
          ></div>
        )}
      </div>
      {contentBaseHeight > baseHeight && (
        <div className="w-fit mt-2 mx-auto">
          <ShowMoreButton isExpanded={isExpanded} setExpanded={setExpanded} />
        </div>
      )}
    </>
  );
}
