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

SS=0
MAX=0
for value in range(1, 26+1):
    answers = min_commands_list(value)
    SS += len(answers)
    MAX=max(MAX, len(answers[0]))
    value_to_variants[value] = answers
    assert all(len(ans)==len(answers[0]) for ans in answers)

# print(SS)
print(MAX)

def transition(cell, cmd):
    h, l = cell
    dh, dl = cmd
    nh, nl = (h + dh) % 6, (l + dl) % 3
    return (nh, nl)


HUE = "rygcbm"
LIGHTNESS = "lnd"

test_input = "abc"
test_cmds = [PUSH, OUT, PUSH, PUSH, ADD, OUT, PUSH, PUSH, ADD, PUSH, ADD, OUT]
test_ans = "nr dr lm nm dm dr lm nm dm dr lr ly nr"

def cmds_to_string(cmds):
    start = 0, 1
    cur = start
    ans = []
    for cmd in cmds + [OUT]:
        i, j = cur
        ans.append(LIGHTNESS[j]+HUE[i])
        cur = transition(cur, cmd)
    return " ".join(ans)

ans = cmds_to_string(test_cmds)
assert ans == test_ans

def hl_to_mask(h, l):
    assert 0 <= h < 6
    assert 0 <= l < 3
    return (1 << h) << (6*l)

assert hl_to_mask(5,0) == 1 << 5
assert hl_to_mask(0,1) == 1 << 6

assert hl_to_mask(5,1) == 1 << 11
assert hl_to_mask(0,2) == 1 << 12

assert hl_to_mask(5,2) == 1 << 17
assert hl_to_mask(5,0) == 1 << 5

from functools import lru_cache
@lru_cache(None)
def get_mask_and_cur(prev, cmds):
    assert cmds
    cur = prev
    mask = 0
    for cmd in cmds:
        cur = transition(cur, cmd)
        mask |= hl_to_mask(*cur)
    assert mask > 0
    return mask, cur

# INPUT_STRING = "abc"
INPUT_STRING = input()
INPUT_VALUES = [ord(c)-ord("a") + 1 for c in INPUT_STRING]
# print(INPUT_VALUES)
N = len(INPUT_STRING)

import heapq
from collections import defaultdict
def get_mask_len(mask):
    return (bin(mask)[2:]).count('1')

# d, pos, mask, h, l
cur = (get_mask_len(hl_to_mask(0,1)), 0, hl_to_mask(0,1), 0, 1)
min_dist = defaultdict(lambda: float("inf"))
state_to_cmd = {}
q = [cur]
ans=None
while q:
    cur = heapq.heappop(q)
    # print(cur)
    d, pos, mask, h, l = cur
    state = (pos, mask, h, l)
    if d > min_dist[state]: # > or done 
        continue
    min_dist[state] = d
    state_to_cmd
    if pos == N:
        ans = d, state
        break
    for variant in value_to_variants[INPUT_VALUES[pos]]:
        prev = (h, l)
        cmds = variant + (OUT, )
        mask_used, nxt = get_mask_and_cur(prev, cmds)
        nxmask = mask | mask_used
        nxd = get_mask_len(nxmask)
        nxstate = (pos+1,nxmask,*nxt)
        if min_dist[nxstate] > nxd:
            min_dist[nxstate] = nxd
            state_to_cmd[nxstate] = state, cmds
            heapq.heappush(q, (nxd, *nxstate))

assert ans
cmdss = []
min_mask_used, state = ans
min_len = sum(len(value_to_variants[value][0])+1 for value in INPUT_VALUES)
while state in state_to_cmd:
    state, cmds = state_to_cmd[state]
    cmdss.append(cmds)
cmdss = cmdss[::-1]
cmds = [cmd for cmds in cmdss for cmd in cmds]

s = cmds_to_string(cmds)
S=set(s.split(" "))
assert len(S) == min_mask_used
assert len(s.split(" ")) == min_len + 1

# print(min_len, min_mask_used)
print(min_len+1)
print(s)