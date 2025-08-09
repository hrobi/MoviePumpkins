"use client";

import { REM } from "@/constants";
import { CirclePlusIcon, SquarePenIcon } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface AddOrModifiyButtonParams {
  text: string;
  href?: string;
  variant?: "add" | "modify";
}

export default function AddOrModifyButton({
  text,
  href,
  variant = "add",
}: AddOrModifiyButtonParams) {
  const [actualWidth, setActualWidth] = useState(1.5 * REM);
  const IconElement = variant == "add" ? CirclePlusIcon : SquarePenIcon;
  return (
    <>
      <Link href={href || ""}>
        <button
          className="overflow-x-hidden block"
          style={{ transition: "width 500ms", width: actualWidth }}
          onMouseEnter={() => setActualWidth(300)}
          onMouseLeave={() => setActualWidth(1.5 * REM)}
        >
          <div className="flex flex-row gap-2 items-center text-xl font-thin group cursor-pointer w-[300px]">
            <IconElement
              size="1.5rem"
              className="group-hover:text-black text-outline"
            />
            {text}
          </div>
        </button>
      </Link>
    </>
  );
}
