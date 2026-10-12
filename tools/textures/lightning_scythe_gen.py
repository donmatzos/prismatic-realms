import math
from diabell_gen import mask, new_grid, shade_blade

def seg(px,py,a,b):
    """distance from (px, py) to segment a-b, and how far along it the closest point lies (0..1)"""
    (x1,y1),(x2,y2)=a,b; dx,dy=x2-x1,y2-y1; t=max(0,min(1,((px-x1)*dx+(py-y1)*dy)/(dx*dx+dy*dy)))
    return math.hypot(px-x1-t*dx,py-y1-t*dy),t
# Blitz Slasher (the Lightning unique scythe): Storm Fang's look (gold blade with a dark amber rim and a white-hot inner edge,
# cyan electric accents) on a storm-slate handle. Blade shading as for the Crimson Diabell (shade_blade's o/1-6 keys).
PAL={".":None,
     "o":"#2e1f04",                                                         # blade rim (dark amber)
     "1":"#6b4a08","2":"#9a6e0e","3":"#c99612","4":"#e8b820","5":"#f6d040","6":"#ffec8a",   # amber -> gold -> light gold edge
     "c":"#9ff0ff","C":"#4cc8e0",                                           # electric cyan: bright, deep
     "k":"#090c14","K":"#1b2130","L":"#2c3446","M":"#465170",               # storm-slate handle: outline, body, light, sheen
     "g":"#7a5a10","G":"#d8a614","H":"#fff3a0"}                             # gold collar and fittings

def shaft(g,top_y,bottom_y,bands,kinks=None,band_key='C'):
    # 3px storm-slate shaft from the blade down (centre x = 16 - y, optionally shifted per row to kink like a bolt)
    for y in range(top_y,bottom_y+1):
        c=16-y+(kinks or {}).get(y,0)
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in bands: g[y][c]=band_key; g[y][c-1]='C' if band_key=='c' else 'g'; g[y][c+1]='C' if band_key=='c' else 'g'
def collar(g):
    g[8][8]='H'; g[8][7]='G'; g[7][9]='G'

CRESCENT=[
    "...########.....",
    ".############...",
    "##############..",
    "#####......#####",
    "###........####.",
    "##..........###.",
    "#..........###..",
    ".........###...."]
def charged_crescent():
    # smooth gold crescent with one large cyan arc crackling along it (few, big branches, bright core fading to deep cyan),
    # neon cyan bands on the slate shaft, gold collar, a small cyan orb at the end
    g=new_grid(); m=mask(CRESCENT); shade_blade(g,m,(8.0,9.6),5.8,9.4)
    arc=[((1.4,3.4),(4.6,1.6)),((4.6,1.6),(7.4,2.6)),((7.4,2.6),(10.6,1.4)),((10.6,1.4),(13.2,3.6)),((13.2,3.6),(12.4,5.8)),((7.4,2.6),(6.6,3.6))]
    for y in range(16):
        for x in range(16):
            if (x,y) in m and g[y][x] not in 'o6' and min(seg(x+.5,y+.5,a,b)[0] for a,b in arc)<0.5:
                g[y][x]='c' if (x+y)%3 else 'C'
    shaft(g,8,13,bands=(10,12),band_key='c'); collar(g)
    for x,y,c in ((1,14,'C'),(2,14,'c'),(1,15,'C'),(2,15,'C'),(3,14,'k')):   # cyan orb
        g[y][x]=c
    return [''.join(r) for r in g]

def paint_arc(g,m,segments,keep='o'):
    # cyan arc over the blade: bright core, every third pixel deep cyan; the dark rim stays
    for y in range(16):
        for x in range(16):
            if (x,y) in m and g[y][x] not in keep and min(seg(x+.5,y+.5,a,b)[0] for a,b in segments)<0.5:
                g[y][x]='c' if (x+y)%3 else 'C'
def orb_end(g):
    for x,y,c in ((1,14,'C'),(2,14,'c'),(1,15,'C'),(2,15,'C'),(3,14,'k')):
        g[y][x]=c
def bolt_end(g):
    for x,y,c in ((1,14,'k'),(2,14,'c'),(3,14,'k'),(1,15,'c'),(2,15,'C')):
        g[y][x]=c

def half_moon(outer,r_out,inner,r_in,clip):
    """crescent-moon blade: inside the outer circle, outside the offset inner circle, limited by clip(x, y)"""
    (ox,oy),(ix,iy)=outer,inner
    return {(x,y) for y in range(16) for x in range(16)
            if math.hypot(x+.5-ox,y+.5-oy)<=r_out and math.hypot(x+.5-ix,y+.5-iy)>r_in and clip(x+.5,y+.5)}
def overcharged_moon(outer=(8.6,9.6),r_out=9.2,inner=(6.2,11.4),r_in=8.0):
    # overcharged without the sparks, on a geometric half-moon blade: smooth outline, thickest at the root (right, where it
    # meets the shaft, ~5px) and tapering evenly to the tip on the left; a short solid tang joins the root to the collar
    g=new_grid()
    m=half_moon(outer,r_out,inner,r_in,lambda x,y: y<8.0 and x<14.6)
    m|={(9,7),(10,7),(11,7),(12,7),(10,6),(11,6),(12,6)}
    shade_blade(g,m,(8.0,9.6),5.8,9.6)
    paint_arc(g,m,[((1.4,3.6),(4.6,1.8)),((4.6,1.8),(7.6,2.8)),((7.6,2.8),(11.0,1.6)),((11.0,1.6),(13.4,4.0)),((13.4,4.0),(12.6,6.0))])
    shaft(g,8,13,bands=(11,),band_key='c')
    g[8][8]='c'; g[8][7]='C'; g[7][9]='C'   # cyan collar
    orb_end(g)
    return [''.join(r) for r in g]

FINAL=overcharged_moon

def in_hand(icon,bands=(13,24)):
    """32px in-hand texture, same treatment as the Crimson Diabell's (diabell_gen.in_hand): blade sweep stretched 6px
    without doubling the dark rim at the left edge, the rest of the head as drawn, a long storm-slate shaft with two cyan
    neon bands, and the icon's orb end at the bottom."""
    g=[['.']*32 for _ in range(32)]
    HEAD,SWEEP_ROWS,SWEEP_COLS,EXTRA=9,6,11,6
    for y in range(HEAD):
        for x in range(16):
            if y<SWEEP_ROWS and x<SWEEP_COLS: continue
            g[y][x+16]=icon[y][x]
        if y<SWEEP_ROWS:
            width=SWEEP_COLS+EXTRA
            for tx in range(width):
                sx=tx*SWEEP_COLS//width; c=icon[y][sx]
                if tx>0 and (tx-1)*SWEEP_COLS//width==sx and c=='o' and (sx==0 or icon[y][sx-1]=='.') and sx+1<16 and icon[y][sx+1] not in '.o':
                    c=icon[y][sx+1]
                g[y][16-EXTRA+tx]=c
    for y in range(HEAD,30):
        c=32-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in bands: g[y][c]='c'; g[y][c-1]='C'; g[y][c+1]='C'
    for y in (14,15):
        for x in range(16):
            if icon[y][x]!='.': g[y+16][x]=icon[y][x]
    return [''.join(r) for r in g]
