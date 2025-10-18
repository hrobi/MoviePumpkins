import { MediaDetailsSection, MediaNotFoundAlert } from "@/features/media/components";
import { fetchMediaDetailsById } from "@/features/media/lib";

export default async function Page({params}: { params: Promise<{ id: number }> }) {
    const {id} = await params;
    const {details, notFound} = await fetchMediaDetailsById(id);
    if (notFound) {
        return <MediaNotFoundAlert id={id}/>;
    } else {
        return <MediaDetailsSection details={details}/>;
    }
}
