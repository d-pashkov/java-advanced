#!/usr/bin/env sh
set -eu

if [ "${COURSE_LIB_DIR:-}" = "" ] || [ ! -d "$COURSE_LIB_DIR" ]; then
  echo "Set COURSE_LIB_DIR to a directory containing the official Java Advanced course JARs." >&2
  exit 2
fi

mkdir -p build/classes
find java-solutions -name '*.java' -print > build/sources.txt
classpath=$(find "$COURSE_LIB_DIR" -name '*.jar' -print | paste -sd ':' -)
javac -encoding UTF-8 -cp "$classpath" -d build/classes @build/sources.txt
find java-solutions -type f -name '*.properties' -print > build/resources.txt
while IFS= read -r resource; do
  target="build/classes/${resource#java-solutions/}"
  mkdir -p "$(dirname "$target")"
  cp "$resource" "$target"
done < build/resources.txt
echo "Compiled $(wc -l < build/sources.txt | tr -d ' ') source files into build/classes"
