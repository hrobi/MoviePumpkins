'use client';

import { useSearchParams } from "next/navigation";
import Link from "next/link";

export default function () {
    const searchParams = useSearchParams();
    const callbackUrl = searchParams.get("callbackUrl") || "/";
    return <Link href={decodeURIComponent(callbackUrl)}>Go Back!</Link>;
}