import math
# Crimson Diabell (blood scythe) icon: hand-drawn fantasy blade silhouettes (round 3), shaded by distance from the crescent's
# centre (dark rim outside -> crimson body -> light honed edge inside), a black handle with gold accents, and a different end
# per candidate. Colour scheme approved in round 2; no blood drips (user request).
PAL={".":None,
     "o":"#22030a",                                   # blade outline
     "1":"#5e0a16","2":"#7d0f1f","3":"#a3172a","4":"#c8243a","5":"#e5414f","6":"#ff7a7f",   # deep crimson -> light red edge
     "d":"#3e0610",                                   # engraved detail (darker than the blade body)
     "r":"#ff3b4a","w":"#ffe3e5",                     # small ruby inset and its glint
     "k":"#060608","K":"#16161c","L":"#2a2a33","M":"#41414d",   # black handle: outline, body, light, sheen
     "g":"#7a5418","G":"#c99a35","H":"#ffe08a"}       # gold accents: shadow, body, shine

def shade_blade(g,mask,center,r_in,r_out):
    cx,cy=center
    rad=lambda x,y: math.hypot(x+.5-cx,y+.5-cy)
    for x,y in mask:
        empty=[(x+i,y+j) for i,j in((1,0),(-1,0),(0,1),(0,-1)) if (x+i,y+j) not in mask]
        r=rad(x,y); t=max(0.0,min(1.0,(r-r_in)/(r_out-r_in)))   # 0 inner edge .. 1 outer rim
        if any(rad(*e)<r-0.3 for e in empty): c='6'               # empty space towards the centre: the honed inner edge
        elif empty: c='o'                                         # empty space outwards: the dark outer rim
        else: c="54321"[min(4,int(t*4.99))]
        g[y][x]=c

def shaft(g,top_y=8,bottom_y=13,bands=(10,)):
    # 3px black shaft from the blade down to the end, one shaft pixel per row (centre x = 16 - y), gold bands on given rows
    for y in range(top_y,bottom_y+1):
        c=16-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in bands: g[y][c]='G'; g[y][c-1]='g'; g[y][c+1]='g'

def collar(g):
    # gold collar where the blade meets the shaft
    g[8][8]='H'; g[8][7]='G'; g[7][9]='G'

def new_grid(): return [['.']*16 for _ in range(16)]

def mask(rows):
    # hand-drawn blade silhouette: '#' = blade
    return {(x,y) for y,r in enumerate(rows) for x,c in enumerate(r) if c=='#'}

def war_moon():
    # broad, heavy crescent with a "bearded" tooth on the inner edge and a spike rising from the root; forked spike at the end
    g=new_grid(); m=mask([
        "...########...#.",
        ".############.##",
        "################",
        "######.....#####",
        "####.#......###.",
        "##..........###.",
        "#..........###..",
        ".........###...."])
    shade_blade(g,m,(8.0,9.6),5.8,9.6)
    shaft(g,8,13,bands=(9,12)); collar(g)
    for x,y,c in ((1,14,'k'),(2,14,'G'),(3,14,'k'),(0,15,'G'),(2,15,'G'),(1,15,'.')):   # forked spike, two gold prongs
        g[y][x]=c
    return [''.join(r) for r in g]

FINAL=war_moon

def in_hand(icon):
    """32px in-hand texture, same treatment as the other unique scythes (sprite_scythe_in_hand.py): the blade's sweep
    (icon rows 0-5, columns 0-10) stretched 6px to the left, the rest of the head (rows 0-8, incl. the gold collar) as
    drawn, then a long black shaft (centre x = 32 - y) with just two gold bands, and the icon's forked end at the bottom."""
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
                repeat=tx>0 and (tx-1)*SWEEP_COLS//width==sx
                # a repeated dark rim pixel on the blade's left edge would make the rim 2px thick: let the blade body
                # (the next pixel inwards) continue there instead, so the blade looks like it extends to the tip
                if repeat and c=='o' and (sx==0 or icon[y][sx-1]=='.') and sx+1<16 and icon[y][sx+1] not in '.o':
                    c=icon[y][sx+1]
                g[y][16-EXTRA+tx]=c
    for y in range(HEAD,30):
        c=32-y
        g[y][c-1]='k'; g[y][c]='M' if y%3==0 else 'L' if y%2 else 'K'; g[y][c+1]='k'
        if y in (13,24): g[y][c]='G'; g[y][c-1]='g'; g[y][c+1]='g'
    for y in (14,15):                 # the icon's end below its last shaft row (13, centre x 3) -> below row 29, same x
        for x in range(16):
            if icon[y][x]!='.': g[y+16][x]=icon[y][x]
    return [''.join(r) for r in g]
