interface TextInputParams {
  placeholder?: string;
  className?: string;
}

export default function TextInput({
  placeholder,
  className = "",
}: TextInputParams) {
  return (
    <>
      <input type="text" className={className} placeholder={placeholder} />
    </>
  );
}
