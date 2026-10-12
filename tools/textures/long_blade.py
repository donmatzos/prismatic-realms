import sys
from PIL import Image
# Reaper-length in-hand blades for the other scythes (user, after Scorpion Tail: "i like the design of the elongated blade
# on the in hand model. draft new versions for each of the others with longer blades"). Each scythe's current 32px in-hand
# texture is the base: its shaft (centre x = 32 - y) and the head features listed in KEEP stay; every other pixel in the top
# rows is erased and a long crescent is drawn instead, between two offset ellipses like Scorpion Tail's (thick at the root,
# tapering to a fine tip far down the left side). It is coloured in depth layers from the outer back to the cutting edge
# in that scythe's own colours, one step darker towards the tip, plus per-scythe accents.
# run from the project root: python3 tools/textures/long_blade.py unused-item-sprites/short-blade-in-hand [out_dir] [suffix]
# (the short-blade originals are the base, so re-running never draws over an already long blade)
T="src/main/resources/assets/prismaticrealms/textures/item"
def rgb(h): return tuple(int(h[i:i+2],16) for i in (1,3,5))+(255,)

# per scythe: rim (outer back), ramp (depth 1.. from the back towards the edge), edge (cutting edge), the ellipses,
# KEEP boxes (x0, y0, x1, y1) of head pixels to keep from the current texture, and an optional accent painter
SPECS={
 "scythe_of_the_frozen_expanse":dict(rim="#042047",ramp=["#042759","#074aae","#0597eb","#78a9e8","#9ae7ff"],edge="#fffac2",
     keep=[(24,1,31,9)]),
 "overgrown_reaper":dict(rim="#002b0f",ramp=["#014d1a","#7bc355","#b6d770","#e0e084","#ebeb96"],edge="#c9c3c9",
     keep=[(24,1,31,9)]),
 "crimson_diabell":dict(rim="#22030a",ramp=["#5e0a16","#7d0f1f","#a3172a","#c8243a","#c8243a"],edge="#ff7a7f",
     keep=[(28,0,31,9)]),
 "blitz_slasher":dict(rim="#2e1f04",ramp=["#9ff0ff","#c99612","#e8b820","#f6d040","#f6d040"],edge="#ffec8a",
     keep=[(26,1,31,9)],thin=True),
 "the_shallow_waters":dict(rim="#0a0a2a",ramp=["#141650","#232a7c","#232a7c","#3346a8","#3346a8"],edge="#6ff5dc",
     keep=[(26,2,31,9)]),
 "melting_sunray":dict(rim="#3d1206",ramp=["#9c2a0e","#d6501a","#f28a22","#ffc23c","#ffc23c"],edge="#fff0a0",
     keep=[(26,0,31,9)]),
 "iron_scythe":dict(rim="#181818",ramp=["#444444","#d8d8d8","#d8d8d8","#ffffff","#ffffff"],edge="#ffffff",
     keep=[(27,2,31,9)]),
}
# scythes whose long in-hand blade now has its own generator (skipped by the main loop)
OWN_GENERATOR={"scythe_of_the_frozen_expanse":"ice_long_gen.py","the_shallow_waters":"ocean_scythe_gen.py (rework)"}
OUTER=((24.0,21.0),24.0,20.0); INNER=((21.0,24.5),21.0,18.0)
THIN_INNER=((22.0,23.5),22.0,19.0)
def inside(e,x,y):
    (cx,cy),rx,ry=e; return ((x+.5-cx)/rx)**2+((y+.5-cy)/ry)**2<=1

def crescent(inner=INNER,clip=lambda x,y: y<=2 or x+y<=29):
    """the long blade: depth of each pixel from the outer back (0 = rim) and the set of cutting-edge pixels.
    clip: which pixels may hold blade; by default it stays left of the shaft below the shaft's top"""
    blade={(x,y) for y in range(20) for x in range(30) if inside(OUTER,x,y) and not inside(inner,x,y) and y<=18 and clip(x,y)}
    depth={}
    frontier=[q for q in blade if any(not inside(OUTER,q[0]+i,q[1]+j) for i,j in ((1,0),(-1,0),(0,1),(0,-1)))]
    for q in frontier: depth[q]=0
    d=0
    while frontier:
        d+=1; nxt=[]
        for x,y in frontier:
            for i,j in ((1,0),(-1,0),(0,1),(0,-1)):
                n=(x+i,y+j)
                if n in blade and n not in depth: depth[n]=d; nxt.append(n)
        frontier=nxt
    edge={(x,y) for (x,y),dd in depth.items() if dd>0 and (any(inside(inner,x+i,y+j) for i,j in ((1,0),(-1,0),(0,1),(0,-1)))
          or (y==18 and (x,y+1) not in blade))}
    return depth,edge
def layer(x,dd,n):
    """ramp index for depth dd (>= 1) with n ramp steps, one step darker towards the tip"""
    return max(0,min(n-1,dd-1-(1 if x<9 and dd>1 else 0)))

def draw(name,spec,base_dir=T):
    base=Image.open(f"{base_dir}/{name}_in_hand.png").convert("RGBA"); bp=base.load()
    img=Image.new("RGBA",(32,32),(0,0,0,0)); p=img.load()
    for y in range(32):                                   # keep the shaft, the lower part and the KEEP boxes
        for x in range(32):
            shaft=abs(x-(32-y))<=1 and y>=2
            kept=any(x0<=x<=x1 and y0<=y<=y1 for x0,y0,x1,y1 in spec["keep"])
            if bp[x,y][3] and (shaft or kept or y>=12): p[x,y]=bp[x,y]
    depth,edges=crescent(THIN_INNER if spec.get("thin") else INNER)
    ramp=[rgb(c) for c in spec["ramp"]]
    for (x,y),dd in depth.items():
        if p[x,y][3]: continue                            # shaft and kept head pixels win
        if dd==0: c=rgb(spec["rim"])
        elif (x,y) in edges: c=rgb(spec["edge"])
        else: c=ramp[layer(x,dd,len(ramp))]
        p[x,y]=c
    return img

if __name__=="__main__":
    # python3 tools/textures/long_blade.py <base_dir with the short-blade *_in_hand.png> [out_dir] [suffix]
    base=sys.argv[1]; out=sys.argv[2] if len(sys.argv)>2 else T; suffix=sys.argv[3] if len(sys.argv)>3 else ""
    for name,spec in SPECS.items():
        if name=="iron_scythe": continue                  # the material scythes come from scythe_gen.long_arched
        if name in OWN_GENERATOR: continue
        draw(name,spec,base).save(f"{out}/{name}_in_hand{suffix}.png")
