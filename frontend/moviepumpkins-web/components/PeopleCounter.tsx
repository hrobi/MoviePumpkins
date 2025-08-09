import { UserRound } from "lucide-react";

interface PeopleCounterProps {
  count: number;
}

export default function PeopleCounter({ count }: PeopleCounterProps) {
  return (
    <>
      <div className="flex flex-row items-end">
        <span className="h-fit">{count}</span>
        <UserRound
          className="mb-[5px]"
          fill="var(--color-outline)"
          strokeWidth={0}
          size="1rem"
        />
      </div>
    </>
  );
}
