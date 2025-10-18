export interface GenericMediaDetails {
    id: number;
    title: string;
    description: string;
    directors?: Array<string>;
    writers?: Array<string>;
    actors?: Array<string>;
    originalTitle?: string;
    countries?: Array<string>;
    awards?: Array<string>;
    totalWins?: number;
    totalNominations?: number;
    posterLink?: string;
}

export interface MovieDetails extends GenericMediaDetails {
    type: "movie";
    releaseYear: number;
    lengthInMinutes?: number;
}

export interface SeriesDetails extends GenericMediaDetails {
    type: "series";
    seasons?: number;
    startedInYear: number;
    endedInYear?: number;
}

export type MediaDetails = MovieDetails | SeriesDetails;
