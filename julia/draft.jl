println("Hello")

struct Rectangle
    x
    y
    color
end

typeof(Rectangle)

L = "Владивосток,🎄,🎄,🎄,Петропавловск-Камчатский,🎄,Южно-Курильск"
A = split(L, ',')
fixedA = [A[i] for i in findall(x->x!="🎄",A)]
fixedL = join(fixedA, ',')
println(fixedL)

function solution()
    n=parse(Int,readline())
    m=parse(Int,readline())
    ans=[]
    for i = max(2,n):m
        ok = true
        for j = 2:min(i-1,floor(Int,sqrt(i))+1)
            if i % j == 0
                ok = false
            end
        end
        if ok
            push!(ans,i)
        end
    end
    println(join(ans, " "))
end

solution()