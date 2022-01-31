[<Measure>] type kg
[<Measure>] type m
[<Measure>] type s
[<Measure>] type N = kg * m / s^2

let G = 6.6743e-11<N * m^2 / kg^2>
let f1(v: int): int = v
let F(m1: float<kg>, m2: float<kg>, r: float<m>): float<N> =
    G * (m1 * m2) / (r * r)

let a = f1 10
let m1 = 3000000.0<kg>
let m2 = 2000000.0<kg>
let r = 5.0<m>
let ans = F (10.0<kg>, 10.0<kg>, 10.0<m>)
printfn "%.20f" ans
let ans2 = F (m1, m2, r)
printfn "%f" ans2