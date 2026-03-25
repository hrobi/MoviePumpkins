export default (noun: string, count: number) => {
  return count == 1 ? noun : noun + "s";
}
