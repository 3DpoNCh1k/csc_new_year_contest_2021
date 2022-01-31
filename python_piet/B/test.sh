for ((i = 1; i <= 10 ; ++i)) {
    echo "Running $i"
    python3.9 gen.py > inp
    python3.9 B_cheat.py < inp > out1
    # python3.9 correct.py < inp > out1
    python3.9 B.py < inp > out2
    diff out1 out2 || exit 1
    cat out1
}
echo "OK!"
