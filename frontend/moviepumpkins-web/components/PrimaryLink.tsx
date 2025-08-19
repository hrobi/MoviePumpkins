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
        primary-btn
        ${className || ""}
    `}
    >
      {children}
    </Link>
  );
}
