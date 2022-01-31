#!/bin/bash

tail -n +3 | tr '[A-Z]' '[a-z]' | cut -f-3,5- -d, | sed '$ d;/^[^,]*\(,[^,]*\)\{,3\}$/d;s/\b\(.\)/\u\1/g' | sort -s -n -t, -k5,5 | awk -F, '{printf "%4d | %-16s | %-3s | %-24s | %-9s\n",NR,$2" "$1,$3,$4,$5;a+=$3;s+=$5}END{printf "\nAvg age: %d\nAvg salary: %d\n",a/NR,s/NR}'
