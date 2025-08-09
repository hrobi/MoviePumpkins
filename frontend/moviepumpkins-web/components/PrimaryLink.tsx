import Link from "next/link";
import React from "react";
import { UrlObject } from "url";

interface PrimaryLinkParams {
  children: React.ReactNode;
  href: string | UrlObject;
  className?: string;
}

export default function PrimaryLink({
  children,
  href,
  className,
}: PrimaryLinkParams) {
  return (
    <Link
      href={href}
      className={`
        bg-secondary 
        py-2 px-6
        rounded-sm 
        text-white 
        hover:bg-secondary-dark
        font-bold
        transition 
        inline-block
        ${className || ""}
    `}
    >
      {children}
    </Link>
  );
}
