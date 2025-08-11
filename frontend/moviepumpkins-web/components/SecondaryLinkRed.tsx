import Link from "next/link";
import { UrlObject } from "url";

interface SecondaryLinkParams {
  children: React.ReactNode;
  href: string | UrlObject;
  className?: string;
}

export default function SecondaryLink({
  href,
  children,
  className = "",
}: SecondaryLinkParams) {
  return (
    <Link
      href={href}
      className={` 
        px-2
        rounded-sm 
        text-red-400
      hover:text-red-600
        font-bold
        transition 
        ${className || ""}
    `}
    >
      {children}
    </Link>
  );
}
