export default (color: string) => {
  return (content: string, name: string, prefix: string, provider: string) => {
      return content.replace(/fill="[^"]*"/g, `fill="${color}"`);
  }
}
