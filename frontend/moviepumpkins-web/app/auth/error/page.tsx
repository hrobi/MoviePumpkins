export default function({ error }: { error: Error }) {
    return <p>${error.message}</p>;
}