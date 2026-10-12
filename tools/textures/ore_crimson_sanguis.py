import math
from ore_gen import blank, stamp, outline, spike, rows, seg
from blood_gen import PAL
TINTS={"q":{"color":"#7a0a16","strength":0.45},"r":{"color":"#5a0610","strength":0.65}}
def vein():
    # a jagged bleeding crack across the stone, small ruby crystals breaking out along it, stone stained red around it
    g=blank(); crack=[(1.0,12.5),(4.5,10.0),(6.5,10.8),(9.5,6.5),(11.5,7.2),(15.0,3.0)]
    for y in range(16):
        for x in range(16):
            d=min(seg(x+.5,y+.5,crack[i],crack[i+1]) for i in range(len(crack)-1))
            if d<0.5: g[y][x]='a'
            elif d<1.6: g[y][x]='q'
            elif d<2.4 and (x*7+y*3)%4==0: g[y][x]='q'
    for pts in ([(3.6,10.6),(4.8,7.4),(6.0,8.0),(5.4,10.8)],[(9.2,7.0),(10.6,3.6),(11.8,4.4),(10.8,7.2)],[(12.6,5.4),(14.4,2.6),(15.4,3.4),(13.8,6.0)]):
        stamp(g,spike(pts))
    for x,y in ((5,8),(11,4)): g[y][x]='w'
    g=outline(g,skip='q')
    for x,y in ((6,12),(6,13),(10,9)): g[y][x]='b'
    return rows(g)
FINAL=vein
