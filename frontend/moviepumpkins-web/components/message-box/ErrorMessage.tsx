import MessageBase from "./MessageBase";

interface ErrorMessageParams {
  title: React.ReactNode | string;
  children: React.ReactNode;
}

export default function ErrorMessage({ title, children }: ErrorMessageParams) {
  return (
    <>
      <MessageBase className="bg-red-500" title={title}>
        {children}
      </MessageBase>
    </>
  );
}
