import random
n = random.randint(1, 100)
s = "".join(chr(ord('a') + random.randint(0, 25)) for i in range(n))
print(s)