math = require "math"
local module = {}
function create_inf_array(n)
    a = {}
    for i=1,n do
        table.insert(a, math.huge)
    end
    return a
end
function module.solver(coins) 
    return function (target)
        dp = create_inf_array(target+1)
        dp[1] = 0
        for i=1, #coins do
            for j=coins[i], target do
                dp[j+1] = math.min(dp[j+1], dp[j-coins[i]+1]+1)
            end
        end
        if dp[target+1] == math.huge then
            return nil
        end
        return dp[target+1]
    end 
end

return module