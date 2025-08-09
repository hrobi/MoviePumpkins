import {
  ChevronDownIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
  ChevronUpIcon,
} from "lucide-react";

interface ShowMoreButtonParams {
  isExpanded: boolean;
  setExpanded: (newState: boolean) => void;
  variant?: "horizontal" | "vertical";
}

export default function ShowMoreButton({
  isExpanded,
  setExpanded,
  variant = "vertical",
}: ShowMoreButtonParams) {
  const NonExpandedElement =
    variant == "vertical" ? ChevronDownIcon : ChevronRightIcon;
  const ExpandedElement =
    variant == "vertical" ? ChevronUpIcon : ChevronLeftIcon;
  return isExpanded ? (
    <ExpandedElement
      className="hover:text-black text-outline cursor-pointer"
      onClick={() => setExpanded(false)}
    />
  ) : (
    <NonExpandedElement
      className="hover:text-black text-outline cursor-pointer"
      onClick={() => setExpanded(true)}
    />
  );
}
