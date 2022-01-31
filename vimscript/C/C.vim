" i
" <C-r>= 

function! Get_tokens(apattern)
let pattern = a:apattern
let l = split(pattern, "[")
let a = l[0]
let b = ""
let n = len(l)
if len(l) > 1
let b = split(l[1], "]")[0]
endif
let ans = ["'" .. a .. "'"]
let i = 0
while i < len(b)
let a = a .. b[i]
let ans += ["'" .. a .. "'"]
let i += 1
endwhile
let ans = join(ans, " | ") .. ";"
return ans
endfunction
