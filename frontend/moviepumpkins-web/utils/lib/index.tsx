export function isBetweenInc(num: number, moreThan: number, lessThan: number) {
  return num >= moreThan && num <= lessThan;
}
export function lineBreakIntoParagraph(text: string) {
  return (
    <>
      {text.split("\n").map((line, index) => (
        <p key={index}>{line}</p>
      ))}
    </>
  );
}
export function take<T>(array: T[], n: number): T[] {
  return array.filter((_, index) => index < n);
}
export function drop<T>(array: T[], n: number): T[] {
  return array.filter((_, index) => index >= n);
}
export function formatCount(count: number): string {
  const countStringLen = count.toString().length;
  const formatDigits = (num: number) =>
    num.toLocaleString(undefined, { maximumFractionDigits: 2 });
  return countStringLen > 1000000
    ? formatDigits(count / 1000000) + "M"
    : isBetweenInc(countStringLen, 1000, 100000)
    ? formatDigits(count / 1000) + "K"
    : count.toString();
}
