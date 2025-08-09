export default function Modal({ children }: { children: React.ReactNode }) {
  return (
    <div
      className="w-full h-[100vh] fixed top-0 overflow-hidden"
      style={{ background: "rgba(0,0,0,0.6)" }}
    >
      {children}
    </div>
  );
}
