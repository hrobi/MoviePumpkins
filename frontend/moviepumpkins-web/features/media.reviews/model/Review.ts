export interface Review {
    id: number;
    user: {
        username: string;
        displayName: string;
    };
    title: string;
    content: string;
    spoilerFree: boolean;
    createdAt: string;
    modifiedAt: string;
    likes: number;
    dislikes: number;
    userOwnRating?: "LIKE" | "DISLIKE" | "NO_RATING" | undefined;
}

export interface Reviews {
    pageCount: number;
    currentPageReviews: Review[];
    page: number;
}
