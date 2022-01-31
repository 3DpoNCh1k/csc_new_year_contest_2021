coins = require("coins")

local solver = coins.solver({ 1,3,4,5 })

for i = 0, 7, 1 do
    print(solver(i))
end
