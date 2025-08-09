import { match } from "ts-pattern";

interface LabelParams {
  text: string;
  className?: string;
  variant?: "gold" | "normal" | "alert";
}

export default function Label({
  text,
  className = "",
  variant = "normal",
}: LabelParams) {
  const variantColoringClassName = match(variant)
    .with("gold", () => "bg-amber-400 text-black")
    .with("normal", () => "bg-orange-400 text-white")
    .with("alert", () => "bg-red-700 text-white")
    .exhaustive();

  return (
    <span
      className={`
            rounded-sm
            w-fit
            py-[2px]
            px-2
            text-md
            text-center
            font-bold
            ${variantColoringClassName}
            ${className}
    `}
    >
      {text}
    </span>
  );
}
