export class UnexpectedAuthErrorResponseError extends Error {
  constructor(status: "unauthorized" | "forbidden") {
    super(
      `Backend api returned ${status} status, but it shouldn't have at this point of the application`
    );
  }
}
