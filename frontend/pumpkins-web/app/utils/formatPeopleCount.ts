export default (count: number) => {
  if (count / 1_000_000 > 1) {
    return `${(count / 1_000_000).toFixed(2)}M`;
  }

  if (count / 1_000 > 1) {
    return `${(count / 1_000).toFixed(2)}K`;
  }

  return `${count}`;
}
