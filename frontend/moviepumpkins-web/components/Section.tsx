import React from "react";

interface SectionParams {
  title: React.ReactNode;
  additionalButton?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
}

export default function Section({
  children,
  title,
  className = "",
  additionalButton,
}: SectionParams) {
  return (
    <>
      <section
        className={`xl:w-2/3 mx-auto rounded-md p-8 mt-5 bg-white border-gray-300 border-[1px] shadow-b-outline ${className}`}
      >
        <h1 className="flex flex-row flex-wrap items-center gap-2 text-3xl border-l-5 border-primary pl-5 font-bold mb-3">
          {title}
          {additionalButton}
        </h1>
        {children}
      </section>
    </>
  );
}
