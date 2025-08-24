export interface Review {
  id: number;
  userDisplayName: string;
  username: string;
  title: string;
  rating: number;
  content: string;
  spoilerFree: boolean;
  userReaction?: "like" | "dislike";
  reactions: {
    likes: number;
    dislikes: number;
  };
}
