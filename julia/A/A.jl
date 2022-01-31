using LinearAlgebra
n=parse(Int,readline())
a=[[parse(Float64,s) for s in split(readline(), " ")] for i = 1:n]
m=parse(Int,readline())
b=[[parse(Float64,s) for s in split(readline(), " ")] for i = 1:m]

function dist(p1, p2)
    return sqrt(dot(p1-p2,p1-p2))
end


function between(l, r, x)
    return min(l,r) <= x <= max(l,r)
end

function solve(point)
    X, Y = point
    # println("point = $point")
    best_point = nothing
    for i = 1:n-1
        o1, o2 = a[i], a[i+1]
        B, A = o1 - o2
        B *= -1
        C = -dot(o1,[A, B])
        d = -(dot([A,B,C], [X, Y, 1]) / dot([A,B],[A,B]))
        P = point + d*[A, B]
        # println("o1 = $o1  o2 = $o2  A = $A  B = $B  C = $C   d = $d  P = $P")
        if !between(o1[1], o2[1], P[1]) || !between(o1[2], o2[2], P[2])
            if dist(P,o1) < dist(P,o2)
                P = o1
            else
                P = o2
            end
        end
        # println("after P = $P")
        # if best_point != nothing
        #     println(dist(best_point, point))
        # end
        # println(dist(P, point))
        if best_point == nothing || dist(best_point, point) > dist(P, point)
            best_point = P
        end
        # println("best p = $best_point")
    end
    return join(map(x -> round(x, digits=3), best_point), " ")
end

println(join(map(solve,b), "\n"))