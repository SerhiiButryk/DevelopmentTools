#!/usr/bin/bash

echo "$#"
echo "$@"

ARRAY=(apple lemon)
ARRAY+=(pear)

echo "${ARRAY[@]}"