import { match } from "ts-pattern";

interface CounterParams {
    title: string
    count: number
    variant: "gold" | "green" | "blue"
    className?: string
}

export default function Counter({ title, variant, count, className = "" }: CounterParams) {

    const bgColor = match(variant)
        .with("gold", () => "bg-amber-400")
        .with("green", () => "bg-green-500")
        .with("blue", () => "bg-blue-400")
        .exhaustive();

    const textColorClass = match(variant)
        .with("gold", () => "text-blue")
        .with("green", () => "text-white")
        .with("blue", () => "text-white")
        .exhaustive();

    return (<>
        <div className={`rounded-md flex flex-row ${bgColor} font-bold ${className}`}>
            <div className={`p-2 text-md grow ${textColorClass}`}>{title}</div>
            <div className={`p-2 pl-0 text-md ${textColorClass}`}><span className="border-l-[1px] border-dashed pl-4">{count}</span></div>
        </div>
    </>);
}