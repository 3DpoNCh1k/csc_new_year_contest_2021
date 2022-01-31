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

INPUT_STRING = input()
INPUT_VALUES = [ord(c)-ord("a") + 1 for c in INPUT_STRING]
N = len(INPUT_STRING)

import heapq
# cnt, pos, mask, h, l
cur = (0, 0, hl_to_mask(0,1), 0, 1)
my_heap = [cur]
state_to_cmd = {}
M = {}
def set_state(cnt, pos, mask, h, l):
    key = pos, mask, h, l
    ok = False
    if key not in M:
        M[key] = cnt
        ok = True
    assert M[key] <= cnt
    return ok

INF=float("inf")
def get_ans():
    min_len = INF
    for (pos,mask,h,l), cnt in M.items():
        if pos == N:
            min_len=min(min_len, cnt)
    assert min_len < INF
    min_mask_used = INF
    for (pos,mask,h,l), cnt in M.items():
        if pos == N and cnt == min_len:
            cur_mask_used = (bin(mask)[2:]).count('1')
            min_mask_used=min(min_mask_used, cur_mask_used)
    assert min_mask_used < INF
    # print("MINS", min_len, min_mask_used)
    ans = []
    for (pos,mask,h,l), cnt in M.items():
        cur_mask_used = (bin(mask)[2:]).count('1')
        if pos == N and cnt == min_len and cur_mask_used == min_mask_used:
            ans.append((cnt, pos, mask, h, l))
    assert ans
    return ans, (min_len, min_mask_used)

# cnt, pos, mask, h, l
while my_heap:
    state = heapq.heappop(my_heap)
    # print(state)
    if set_state(*state):
        # print("set")
        cnt, pos, mask, h, l = state
        if pos < N:
            value = INPUT_VALUES[pos]
            for variant in value_to_variants[value]:
                prev = (h,l)
                cmds = variant + (OUT, )
                used_mask, nxt_last = get_mask_and_cur(prev, cmds)
                nxt = (cnt + len(cmds), pos + 1, mask | used_mask, *nxt_last)
                state_to_cmd[nxt] = (state, cmds)
                heapq.heappush(my_heap,nxt)

# mask, h, l, cnt
answers, (min_len, min_mask_used) = get_ans()
for ans in answers:
    # mask, h, l, cnt = ans
    # state = cnt, N, mask, h, l
    state = ans
    # print(state)
    # print(state_to_cmd)
    cmds = []
    while state in state_to_cmd:
        state, cmd = state_to_cmd[state]
        cmds.append(cmd)
    cmds = cmds[::-1]
    # print(cmds)
    cmds = [cmd for cmdss in cmds for cmd in cmdss]
    # print(cmds)
    s = cmds_to_string(cmds)
    # print(s)
    S = set(s.split(" "))
    cur_mask_len = (bin(ans[2])[2:]).count('1')
    # print(len(S), cur_mask_len)
    assert len(S) == cur_mask_len, (S, bin(ans[2]), cur_mask_len)
    assert len(S) == min_mask_used
    assert len(s.split(" ")) == cnt + 1, (s, cnt)
    # assert False

print(min_len, min_mask_used)