import { Rating } from "./Ratings";

export interface Award {
  type: string;
  count: number;
}

export interface Awards {
  namedAwards: Award[];
  totalWinCount?: number;
  totalNominationCount?: number;
}

export interface MediaBaseDetails {
  averageRating?: Omit<Rating, "label">;
  title: string;
  shortDescription: string;
  pictureHref: string;
  directors: string[];
  writers: string[];
  actors: string[];
  country?: string;
  awards?: Awards;
}

export interface MovieDetails extends MediaBaseDetails {
  runtimeInMinutes: number;
}

export interface SeriesDetails extends MediaBaseDetails {
  seasonCount: number;
  episodeCount: number;
}

export type MediaDetails = MovieDetails | SeriesDetails;
