#!/bin/sh
set -eu

script_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
repo_root=$(CDPATH= cd -- "$script_dir/.." && pwd)
source_dir="$repo_root/backend/backend-service/src/main/kotlin"
messages_dir="$repo_root/backend/backend-service/src/main/resources/messages"

if [ ! -d "$source_dir" ]; then
    printf 'Business exception source directory not found: %s\n' "$source_dir" >&2
    exit 1
fi

if [ ! -d "$messages_dir" ]; then
    printf 'Error messages directory not found: %s\n' "$messages_dir" >&2
    exit 1
fi

declarations=$(grep -R -h -E --include='*.kt' \
    '^[[:space:]]*(public[[:space:]]+|internal[[:space:]]+)?class[[:space:]]+[A-Za-z_][A-Za-z0-9_]*.*:[[:space:]]*(ResourceNotFoundException|ResourceConflictException|AuthorizationException)([[:space:]]*\(|[[:space:]]*$)' \
    "$source_dir" || true)

if [ -z "$declarations" ]; then
    printf 'No direct business exception subclasses were found under %s\n' "$source_dir" >&2
    exit 1
fi

exception_classes=$(printf '%s\n' "$declarations" \
    | sed -E 's/.*class[[:space:]]+([A-Za-z_][A-Za-z0-9_]*).*/\1/' \
    | sort -u)

set -- "$messages_dir"/error_messages_*.properties
if [ ! -f "$1" ]; then
    printf 'No validation error messages files found under %s\n' "$messages_dir" >&2
    exit 1
fi

failed=0
for exception_class in $exception_classes; do
    key=${exception_class%Exception}

    for message_file do
        [ -f "$message_file" ] || continue

        if ! grep -Eq "^[[:space:]]*${key}([[:space:]]*[:=]|[[:space:]]|$)" "$message_file"; then
            printf 'Missing translation key "%s" in %s\n' "$key" "$message_file" >&2
            failed=1
        fi
    done
done

exit "$failed"
