const assertNever = (_: never): never => {
  throw new Error("Program should not have reached here!");
}

export default assertNever;