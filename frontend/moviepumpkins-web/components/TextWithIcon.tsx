interface TextWithIconParams {
  text: string;
  icon: React.ReactNode;
}

export default function TextWithIcon({ text, icon }: TextWithIconParams) {
  return (
    <span className="flex flex-row gap-1">
      {icon}
      {text}
    </span>
  );
}
