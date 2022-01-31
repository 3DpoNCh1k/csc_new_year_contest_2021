for((i=1;i<=10;++i)) {
    echo "Running $i"
    python3.9 gen.py > inp
    python3.9 correct.py < inp > out
    ocaml A.ml < inp > my_out
    diff out my_out
    cat inp out my_out
}
echo "OK"