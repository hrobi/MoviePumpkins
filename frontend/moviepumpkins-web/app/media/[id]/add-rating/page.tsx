"use client";

import Modal from "@/components/Modal";
import Section from "@/components/Section";
import { XCircleIcon } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { createPortal } from "react-dom";

export default function Page() {
  const [loaded, setLoaded] = useState(false);
  const router = useRouter();
  useEffect(() => setLoaded(true), []);
  return (
    loaded &&
    createPortal(
      <Modal>
        <Section
          title="Add your own review"
          additionalButton={
            <button className="cursor-pointer" onClick={() => router.back()}>
              <XCircleIcon />
            </button>
          }
        >
          Add your own review!
        </Section>
      </Modal>,
      document.body
    )
  );
}
