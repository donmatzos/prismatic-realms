# Scorpion Tail, the Venom Crown unique scythe (Poison theme), based on the user's unused sprite unused-item-sprites/galaxy_scythe.png
# (user direction: "take that as a base"): its mottled round blade, steel back with a gem and dark hooked spur are kept,
# recoloured into the Venom Crown palette (plum rim, amethyst body, acid-green venom accent).
BASE=[
    "....aaaaabb.....",
    "..aaccddeecab...",
    ".accdffgfhiccb..",
    ".jifkllmkkfiina.",
    "jdkml...lmgkhob.",
    "jhl.......mfpqoa",
    "rm.........mosqa",
    "t......uuu.msqoa",
    ".....uuvvwuqoxsa",
    "....uwyuuyvqxzxb",
    "...Awuu..uuBsxqb",
    "..uwu.......Bob.",
    "..uwu.......Bob.",
    ".uyu.........b..",
    "uvu.............",
    "uu.............."]
# the galaxy sprite's colours (keys above) -> Venom Crown colours
PAL={".":None,
     "a":"#1f0a2e","b":"#14081e","A":"#0a0410","B":"#1a0e22",                 # outlines
     "c":"#2e1047","e":"#331350","j":"#381556","r":"#3e1860","d":"#4a1a6e",   # deep plum
     "i":"#5a2186","h":"#682896","g":"#7a2fa8","k":"#a95bd6",                 # amethyst
     "m":"#4e1a5a","t":"#5c1f66","f":"#86307e","l":"#9a3a8c",                 # mauve mottle (was the galaxy's magenta)
     "n":"#2c1c38","o":"#3c2a4a","s":"#5e4870","p":"#c9a6e0","q":"#e6d2f4",   # pale amethyst-steel back
     "x":"#b6f23c","z":"#5fb820",                                             # acid-green gem (was violet)
     "u":"#0e0418","v":"#1f0a2e","w":"#4a1a6e","y":"#2e1047",                 # plum-black shaft
     "V":"#b6f23c","G":"#5fb820","W":"#e4ffb0"}                               # venom accents: bright, dark, glint

def grid(): return [list(r) for r in BASE]
def rows(g): return [''.join(r) for r in g]

def recolour():
    # the galaxy scythe as it is, only recoloured: mottled plum/amethyst blade, pale amethyst-steel back, acid-green gem
    return rows(grid())

def venom_edge():
    # the recolour with the blade's inner (cutting) edge glowing acid green, and venom beading off the tip in two drops
    g=grid()
    for x,y,c in ((2,4,'V'),(8,4,'V'),(9,4,'G'),(2,5,'G'),(10,5,'V'),(1,6,'V'),(11,6,'G'),(0,7,'V')):
        g[y][x]=c
    g[9][0]='V'; g[11][0]='G'
    return rows(g)

def toxic_mottle():
    # the recolour where the galaxy's mottle turns toxic: a few acid-green blotches in the blade like poison seeping
    # through and a pale green glint on the steel back
    g=grid()
    for x,y,c in ((6,2,'G'),(7,2,'V'),(3,3,'G'),(9,3,'V'),(10,3,'G'),(1,4,'G'),(12,4,'V')):
        g[y][x]=c
    g[5][13]='W'
    return rows(g)


FINAL=toxic_mottle

# In-hand texture (32px), drawn fresh rather than stretched (user: "the blade should be extra long like a reaper's scythe,
# and the handle is a bit warped" as in the icon). The icon's steel back, gem and spur are kept at the same pixel size at the
# right; the blade is a long reaper crescent between two offset ellipses (thick at the root, tapering to a fine tip far down
# the left side), coloured in layers like the icon (plum rim, deep plum, mottle, mauve inner edge) with toxic blotches; the
# handle leaves the gem at a shallower angle and bends slightly a third of the way down; the rest runs straight (user).
import math
OUTER=((24.0,21.0),24.0,20.0)
INNER=((21.0,24.5),21.0,18.0)
def _in(e,x,y):
    (cx,cy),rx,ry=e; return ((x+.5-cx)/rx)**2+((y+.5-cy)/ry)**2<=1
MOTTLE="ghfkilgkhfmikg"
TOXIC={(13,5,'G'),(14,5,'V'),(9,7,'V'),(17,4,'V'),(18,4,'G'),(21,3,'G'),(6,9,'G'),(24,5,'V')}
SNATH=[(25.5,9.5),(16.8,15.6),(1.2,31.6)]   # one minor bend a third of the way down, straight after it (user)
def _seg_dist(px,py,a,b):
    (ax,ay),(bx,by)=a,b; dx,dy=bx-ax,by-ay
    t=max(0,min(1,((px-ax)*dx+(py-ay)*dy)/(dx*dx+dy*dy)))
    return math.hypot(px-ax-t*dx,py-ay-t*dy)
def in_hand():
    g=[['.']*32 for _ in range(32)]
    blade={(x,y) for y in range(32) for x in range(28) if _in(OUTER,x,y) and not _in(INNER,x,y) and y<=18}
    for x,y in blade:
        nb=[(x+i,y+j) for i,j in ((1,0),(-1,0),(0,1),(0,-1))]
        if any(not _in(OUTER,*n) for n in nb): c='b' if x>22 else 'a'
        elif any(_in(INNER,*n) for n in nb) or (x,y+1) not in blade and y==18: c="mlt"[(x+y)%3]
        elif any(not _in(OUTER,*n) for n in [(x+i,y+j) for i,j in ((2,0),(-2,0),(0,2),(0,-2),(1,-1),(-1,-1))]): c="cdejr"[(x*7+y)%5]
        else: c=MOTTLE[(x*37+y*101+(x*y)%7*13)%len(MOTTLE)]   # scattered, not striped
        g[y][x]=c
    for x,y,c in TOXIC:
        if (x,y) in blade and g[y][x] not in 'ab': g[y][x]=c
    prev=None
    for y in range(9,32):                                        # snath: per row outline, core, outline (like the other shafts);
        for a,b in zip(SNATH,SNATH[1:]):                         # where it runs flatter than 45 degrees the core spans the step
            if a[1]<=y+.5<=b[1]:
                c=round(a[0]+(b[0]-a[0])*(y+.5-a[1])/(b[1]-a[1])-.5); break
        lo,hi=(c,prev-1) if prev is not None and prev-1>c else (c,c)
        for x in range(lo-1,hi+2):
            k='u' if x in (lo-1,hi+1) else "wyv"[(x+y)%3]
            if 0<=x<32 and g[y][x]=='.': g[y][x]=k
        prev=c
    icon=FINAL()
    for y in range(14):                                          # the icon's steel back, gem and spur, unchanged
        for x in range(11,16):
            if icon[y][x]!='.': g[y][x+16]=icon[y][x]
    return [''.join(r) for r in g]
