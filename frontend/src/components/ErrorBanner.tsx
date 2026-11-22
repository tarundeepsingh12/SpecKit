import { ApiError } from "@/lib/api";

export function ErrorBanner({ error }: { error: ApiError | string | null }) {
  if (!error) return null;

  if (typeof error === "string") {
    return <div className="banner error">{error}</div>;
  }

  return (
    <div className="banner error">
      <strong>{error.error}:</strong> {error.message}
      {error.fieldErrors.length > 0 && (
        <ul>
          {error.fieldErrors.map((fieldError) => (
            <li key={fieldError.field}>
              {fieldError.field}: {fieldError.message}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
