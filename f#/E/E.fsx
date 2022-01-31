type Expression =
    | Plus of Expression * Expression
    | Minus of Expression * Expression
    | Multiply of Expression * Expression
    | Divide of Expression * Expression
    | Literal of float


let expr =
    let ``5`` = Literal 5
    let ``50`` = Literal 50
    let h = (Plus(``50``, ``50``))
    let a = Plus ((Divide ((Plus(``50``, ``5``)),
               ``5``)), ``5``)
    Divide(Plus(h, Divide(Multiply(Plus(a, a), ``50``), ``5``)), Literal 10)

let rec calculate exp =
    match exp with
    | Plus (l, r) -> calculate l + calculate r
    | Minus (l, r) -> calculate l - calculate r
    | Multiply (l, r) -> calculate l * calculate r
    | Divide (l, r) -> calculate l / calculate r 
    | Literal x -> x 

let ans = calculate expr

System.Console.WriteLine ans