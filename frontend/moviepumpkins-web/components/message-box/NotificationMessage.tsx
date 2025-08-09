import MessageBase from "./MessageBase";

interface NotificationMessageParams {
  title: React.ReactNode | string;
  children: React.ReactNode;
}

export default function NotificationMessage({
  title,
  children,
}: NotificationMessageParams) {
  return (
    <>
      <MessageBase className="bg-black" title={title}>
        {children}
      </MessageBase>
    </>
  );
}
