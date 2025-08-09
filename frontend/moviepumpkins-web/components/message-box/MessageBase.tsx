interface MessageBaseParams {
  title: React.ReactNode | string;
  children: React.ReactNode;
  className: string;
}

export default function MessageBase({
  title,
  children,
  className,
}: MessageBaseParams) {
  return (
    <>
      <div className={`py-4 px-6 text-white rounded-md ${className}`}>
        <h2 className="font-bold pb-2">{title}</h2>
        <div>{children}</div>
      </div>
    </>
  );
}
