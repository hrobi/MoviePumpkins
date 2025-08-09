import { coreClient } from "@/api-client";

export default async function Page({
    params,
}: {
    params: Promise<{ username: string }>
}) {
    const { username } = await params;
    const { error, data: userProfile } = await coreClient.GET("/users/{username}/profile", {
        params: { path: { username: username } }
    })

    return (error ? <p>{error.reason}</p> : <>
        <p>username: {userProfile.username}</p>
        <p>email: {userProfile.email}</p>
        <p>display name: {userProfile.displayName}</p>
        <p>full name: {userProfile.fullName}</p>
        <p>user role: {userProfile.role}</p>
        <p>about: {userProfile.about === "" ? "Give a short description about yourself!" : userProfile.about}</p>
    </>);
}