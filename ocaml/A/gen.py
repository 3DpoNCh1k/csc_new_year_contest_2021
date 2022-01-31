import random

in1="gbp"
in2="gbpbp"
in3="bgbpp"
in4="gbpbpbbbpbpppbp"
in5="b"
inputs=[in1,in2,in3,in4]
letters = "gbp"
random_s = "".join(random.choices(letters, k=random.randint(1,10)))
# if random.randint(1, 10) >= 6:
#     inputs.append(random_s)
n = random.randint(10000, 10000)
s = "".join(random.choices(inputs, k=n)) + "p" * (n-1)
print(s)