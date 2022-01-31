PUSH = (0, 1)
ADD = (1, 0)
MUL = (1, 2)
DUP = (4, 0)
OUT = (5, 1)

commands = [
    PUSH,
    ADD,
    MUL,
    DUP,
    OUT,
]

commands_to_string = {
    PUSH: "PUSH",
    ADD: "ADD",
    MUL: "MUL",
    DUP: "DUP",
    OUT: "OUT",
}

def print_commands(ans):
    cmds = []
    for cmd in ans:
        cmds.append(commands_to_string[cmd])
    print(", ".join(cmds))

def min_commands_list(value):
    L = 15
    for l in range(1, L + 1):
        cur_cmds = []
        cur_stack = []
        ans = []
        def solve():
            nonlocal cur_stack
            if len(cur_cmds) == l:
                if len(cur_stack) == 1 and cur_stack[0] == value:
                    ans.append(tuple(cur_cmds))
                return
            for cmd in commands:
                if cmd == PUSH:
                    cur_stack.append(1)
                    cur_cmds.append(PUSH)
                    solve()
                    cur_stack.pop()
                    cur_cmds.pop()
                if cmd in [ADD, MUL] and len(cur_stack) >= 2:
                    a, b = cur_stack[-2:]
                    cur_stack = cur_stack[:-2]
                    val = a + b if cmd == ADD else a * b
                    cur_stack.append(val)
                    cur_cmds.append(cmd)
                    solve()
                    cur_stack.pop()
                    cur_stack.extend([a,b])
                    cur_cmds.pop()
                    assert [a,b] == cur_stack[-2:]
                if cmd == DUP and len(cur_stack) >= 1:
                    cur_stack.append(cur_stack[-1])
                    cur_cmds.append(cmd)
                    solve()
                    cur_stack.pop()
                    cur_cmds.pop()
        solve()
        if ans:
            return ans
    assert False, f"l > {L}"    


value_to_variants = [[]]*30 


for value in range(1, 26+1):
    answers = min_commands_list(value)    
    value_to_variants[value] = answers
    assert all(len(ans)==len(answers[0]) for ans in answers)

f = open("table.txt", "w")
strs = ["["]
for value in range(30):
    row = ["\t["]
    for variant in value_to_variants[value]:
        row.append(f"\t\t{variant},")
    row.append("\t]")
    row = "\n".join(row)
    strs.append(f"{row},")
strs.append("]")
s="\n".join(strs)
f.write(s)

