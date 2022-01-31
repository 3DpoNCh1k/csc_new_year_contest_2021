#! /usr/bin/bash

for((i = 1; i <= 5; ++i)) {
    echo "Running $i"
    bash command.sh < in$i > out
    perl B.pl < in$i > my_out
    diff out my_out || exit 1
}

echo "OK"