B_IVT = [170, 187, 110]
B_ITSS = [55+50, 61 + 55, 50 + 55]
ENE = [15, 17, 10]
B_RAD = [20+20, 22 + 22, 40 + 40]
B_KIT = [30+17,34+20, 30 + 1]
IST = [30, 33, 20]
PI = [30, 33, 20]
FI = [35, 39, 20]
TB = [0, 0, 25]
PMI = [10, 11, 20]
BI = [0, 0, 15]
B_MNG = [0, 0, 50]
ECO = [0, 0, 50]
SOC = [0, 0, 50]
REK = [0, 0, 70]
IB = [0, 0, 25]

ALL = [B_IVT, B_ITSS, ENE, B_RAD, B_KIT, IST, PI, FI, TB, PMI, BI, B_MNG, ECO, SOC, REK, IB]
print([(e[0], sum(e[1:])) for e in ALL])
assert len(ALL) == 16
assert all(len(x)==3 for x in ALL)
assert sum(x[0] for x in ALL) == 170+55+50+15+20+30+17+30+30+35+10+20
assert sum(x[1] for x in ALL) == 187+61+55+17+22+34+20+33+33+39+11+22
assert sum(x[2] for x in ALL) == 110+50+55+10+40+30+1+20+20+20+25+20+15+50+50+50+70+25+40

ans = sum(x[0]/sum(x[1:]) for x in ALL) / len(ALL)
print(ans)