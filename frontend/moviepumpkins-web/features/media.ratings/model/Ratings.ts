export interface Rating {
    flavour: {
        name: string;
        id: string;
        description: string;
    };
    score: number;
    count: number;
}

export interface Ratings {
    pageCount: number;
    page: number;
    ratings: Rating[];
}
