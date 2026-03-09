export default (minutes: number) => {
  const hours = Math.floor(minutes / 60);
  return hours == 0 ? `${minutes}h` : `${hours}h ${minutes % 60}`;
}
