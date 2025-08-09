export interface Review {
  id: number;
  userDisplayName: string;
  username: string;
  title: string;
  rating: number;
  content: string;
  spoilerFree: boolean;
  usefulness: {
    likes: number;
    dislikes: number;
  };
}
