import { CheckIcon } from "lucide-react";

export default function SuccessAlert({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex flex-row gap-2 rounded py-2 px-4 bg-green-700 text-white">
      <CheckIcon color="white" />
      {children}
    </div>
  );
}
