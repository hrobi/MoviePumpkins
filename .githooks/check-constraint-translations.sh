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
    '^[[:space:]]*(public[[:space:]]+|internal[[:space:]]+)?(class|object)[[:space:]]+[A-Za-z_][A-Za-z0-9_]*.*:[[:space:]]*Constraint' \
    "$source_dir" || true)

if [ -z "$declarations" ]; then
    printf 'No direct constraint subclasses were found under %s\n' "$source_dir" >&2
    exit 1
fi

constraints=$(printf '%s\n' "$declarations" \
    | sed -E 's/.*(class|object)[[:space:]]+([A-Za-z_][A-Za-z0-9_]*).*/\2/' \
    | sort -u)

set -- "$messages_dir"/validation_error_messages_*.properties
if [ ! -f "$1" ]; then
    printf 'No error messages files found under %s\n' "$messages_dir" >&2
    exit 1
fi

failed=0
for constraint in $constraints; do
    key=${constraint%Constraint}

    for message_file do
        [ -f "$message_file" ] || continue

        if ! grep -Eq "^[[:space:]]*${key}([[:space:]]*[:=]|[[:space:]]|$)" "$message_file"; then
            printf 'Missing translation key "%s" in %s\n' "$key" "$message_file" >&2
            failed=1
        fi
    done
done

exit "$failed"
