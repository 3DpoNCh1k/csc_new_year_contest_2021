class Box:
    def __init__(self):
        self.content = []
    def put(self, something):
        self.content.append(something)
    def __str__(self):
        s = ";".join(str(c) for c in reversed(self.content))
        return f"Box[{s}]"

class Gift:
    def __str__(self):
        return "Gift"

table=[]
FAILED=False

def process(c):
    global FAILED, table
    if not FAILED:
        if c == 'p':
            if len(table) >= 2 and isinstance(table[-1], Box):
                box = table.pop()
                something = table.pop()
                box.put(something)
                table.append(box)
            else:
                FAILED=True
        elif c == 'b':
            table.append(Box())
        elif c == 'g':
            table.append(Gift())
        else:
            FAILED=True

s=input()
# print(s)
for c in s:
    process(c) 

if (len(table) != 1) or not isinstance(table[0], Box):
    FAILED=True

print(table[0] if not FAILED else "fail")  