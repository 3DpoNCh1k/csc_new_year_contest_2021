// dotnet fsi fib.fsx

let fibonacci n =
    match n with
    | 0 | 1 -> n
    | _ ->
        let sq = seq {0..n-2 } in 
        let f acc value =
            let a :: b :: tail = acc
            [b; a + b]
        in
        let a = Seq.fold f [0;1] sq in
        a[1]

let n = System.Convert.ToInt32(System.Console.ReadLine())
printfn "%d" (fibonacci n)