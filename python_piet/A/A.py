import sys
from functools import lru_cache
cache = lru_cache(None)
if __name__ == '__main__':
    matrix = [[(1 if num == 'F' else 0) for num in list(line.strip('\n'))] for line in sys.stdin]
    n = len(matrix)
    m = len(matrix[0])
    @cache
    def solve(r,c,h,w,p):
        if h == 1 and w == 1:
            return matrix[r][c] == p
        ans = 0
        for i in range(w-1):
            w1 = i + 1
            w2 = w - w1
            if w1 <= w2:
                ans = ans or not solve(r,c+w1,h,w2,1-p)
            if w2 <= w1:
                ans = ans or not solve(r,c,h,w1,1-p)
        for i in range(h-1):
            h1 = i + 1
            h2 = h - h1
            if h1 <= h2:
                ans = ans or not solve(r+h1,c,h2,w,1-p)
            if h2 <= h1:
                ans = ans or not solve(r,c,h1,w,1-p)
        # print(r,c,h,w,ans,p)
        return ans
    print("YES" if solve(0,0,n,m,1) else "NO")
        
            