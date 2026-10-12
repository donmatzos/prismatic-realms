import glob, io, math, os, sys, zipfile
from PIL import Image
# Mob effect icons (18x18, assets/prismaticrealms/textures/mob_effect/<id>.png) for the scythe effects, inspired by
# vanilla's effect icons (user: "use the original icons for inspiration"). Three directions per effect:
#  A = a fitting vanilla icon recoloured into the effect's palette by brightness (vanilla read from the client jar),
#  B = a new symbol drawn in vanilla's style (dark outline, lit from the top left),
#  C = the effect's crystal family shard (our own item texture), centred.
# run from the project root: python3 tools/textures/effect_icons.py [candidates [out_dir]]
OUT="src/main/resources/assets/prismaticrealms/textures/mob_effect"
ITEMS="src/main/resources/assets/prismaticrealms/textures/item"
def hexc(h): return tuple(int(h[i:i+2],16) for i in (1,3,5))+(255,)

def vanilla(name):
    jar=sorted(glob.glob(os.path.expanduser("~/.gradle/caches/fabric-loom/*/minecraft-client.jar")))[-1]
    with zipfile.ZipFile(jar) as z:
        return Image.open(io.BytesIO(z.read(f"assets/minecraft/textures/mob_effect/{name}.png"))).convert("RGBA")

def recolour(img,ramp,accent=None):
    """map each pixel's brightness onto ramp (dark -> light); accent=(predicate on the original rgb, colour) recolours
    matching pixels separately, e.g. to keep a highlight"""
    out=img.copy(); p=out.load(); R=[hexc(c) for c in ramp]
    lum=[0.299*r+0.587*g+0.114*b for x in range(18) for y in range(18) for r,g,b,a in [img.getpixel((x,y))] if a]
    lo,hi=min(lum),max(lum)
    for y in range(18):
        for x in range(18):
            r,g,b,a=p[x,y]
            if not a: continue
            if accent and accent[0]((r,g,b)): p[x,y]=hexc(accent[1])[:3]+(a,); continue
            t=(0.299*r+0.587*g+0.114*b-lo)/max(1,hi-lo)
            p[x,y]=R[min(len(R)-1,int(t*len(R)))][:3]+(a,)
    return out

def shaded(mask,ramp,img=None):
    """vanilla-style shape: ramp[0] outline on the mask's border, body shaded from light (top left) to dark, one highlight"""
    img=img or Image.new("RGBA",(18,18),(0,0,0,0)); p=img.load(); R=[hexc(c) for c in ramp]
    xs=[x for x,y in mask]; ys=[y for x,y in mask]
    for x,y in mask:
        if any((x+i,y+j) not in mask for i,j in ((1,0),(-1,0),(0,1),(0,-1))): p[x,y]=R[0]; continue
        t=((x-min(xs))/max(1,max(xs)-min(xs))+(y-min(ys))/max(1,max(ys)-min(ys)))/2   # 0 lit (top left) .. 1 shadow
        p[x,y]=R[len(R)-1-min(len(R)-2,int(t*(len(R)-1)))]
    return img

def circle(cx,cy,r): return {(x,y) for x in range(18) for y in range(18) if math.hypot(x+.5-cx,y+.5-cy)<=r}
def drop(cx,cy,r,h):
    """teardrop: a circle with a point h pixels above it"""
    m=circle(cx,cy,r)
    for y in range(18):
        for x in range(18):
            dy=cy-(y+.5)
            if 0<dy<=r+h and abs(x+.5-cx)<=r*(1-(dy-0)/(r+h))*1.05 and y+.5<cy: m.add((x,y))
    return m
def poly(points):
    def inside(px,py):
        c=False; n=len(points)
        for i in range(n):
            (x1,y1),(x2,y2)=points[i],points[(i+1)%n]
            if (y1>py)!=(y2>py) and px<(x2-x1)*(py-y1)/(y2-y1)+x1: c=not c
        return c
    return {(x,y) for x in range(18) for y in range(18) if inside(x+.5,y+.5)}
def line(a,b,w=0.6):
    (ax,ay),(bx,by)=a,b
    def d(px,py):
        dx,dy=bx-ax,by-ay; t=max(0,min(1,((px-ax)*dx+(py-ay)*dy)/(dx*dx+dy*dy)))
        return math.hypot(px-ax-t*dx,py-ay-t*dy)
    return {(x,y) for x in range(18) for y in range(18) if d(x+.5,y+.5)<=w}
def put(img,pixels,colour):
    p=img.load()
    for x,y in pixels:
        if 0<=x<18 and 0<=y<18: p[x,y]=hexc(colour)
    return img

def shard(family):
    im=Image.open(f"{ITEMS}/{family}_shard.png").convert("RGBA")
    out=Image.new("RGBA",(18,18),(0,0,0,0)); out.paste(im,(1,1),im); return out

# ---- B symbols
def snowflake():
    # six arms with a pair of branches each; radial gradient, dark blue halo as the outline
    img=Image.new("RGBA",(18,18),(0,0,0,0)); c=(9,9); arms=set()
    for k in range(6):
        a=math.radians(90+60*k); e=(9+7.6*math.cos(a),9-7.6*math.sin(a)); arms|=line(c,e,0.7)
        for f in (0.55,):
            m=(9+7.6*f*math.cos(a),9-7.6*f*math.sin(a))
            for s in (-1,1):
                b=math.radians(90+60*k+40*s); arms|=line(m,(m[0]+2.6*math.cos(b),m[1]-2.6*math.sin(b)),0.55)
    halo={(x+i,y+j) for x,y in arms for i,j in ((1,0),(-1,0),(0,1),(0,-1))}-arms
    put(img,halo,"#1d4f8c")
    # gradient (user: "add some gradient"): white at the centre through ice blue to deeper blue at the arm tips,
    # one step lighter on the top-left half
    ramp=["#ffffff","#d8f6ff","#9ae7ff","#6cc4f0","#3f8fd6"]
    for x,y in arms:
        r=math.hypot(x+.5-9,y+.5-9)/7.8; k=min(4,int(r*5)); k=max(0,k-1) if x+y<17 else k
        put(img,{(x,y)},ramp[k])
    return img
def seed():
    img=Image.new("RGBA",(18,18),(0,0,0,0))
    body={(x,y) for x in range(18) for y in range(18) if ((x+.5-8)/4.2)**2+((y+.5-12)/4.8)**2<=1}
    shaded(body,["#3a2410","#5e3a1a","#8a5a2b","#b07a40","#d8a868"],img)
    stem=line((8.5,7.5),(9.5,3.5),0.6); put(img,stem,"#2f7a1e")
    leafL=poly([(9,4),(5,2),(3.5,4.5),(7,5.5)]); leafR=poly([(9.5,4),(13,1.5),(15,3.5),(11,5.5)])
    shaded(leafL|leafR,["#1f5212","#3a8a24","#5fb83a","#8fdc5a"],img)
    d=drop(14,14.2,2.2,2.2); shaded(d,["#4a0a12","#8a1020","#c42030","#ff5a5a"],img)
    return img
def blood_drop():
    img=Image.new("RGBA",(18,18),(0,0,0,0))
    shaded(drop(9,11.4,5.2,6),["#2e0308","#5e0a16","#8a1020","#b81c2c","#e5414f"],img)
    put(img,{(6,9),(6,10),(7,8)},"#ff9aa0")
    return img
def bolt():
    img=Image.new("RGBA",(18,18),(0,0,0,0))
    shaded(poly([(11,0.5),(3.5,10),(8.2,10),(6,17.5),(14.5,7),(9.6,7),(12.5,0.5)]),["#5a3d04","#b8860b","#e8b820","#f6d040","#fff2a0"],img)
    return img
def tide_drop():
    img=Image.new("RGBA",(18,18),(0,0,0,0))
    shaded(drop(9,11.4,5.2,6),["#0b2a4a","#145a7a","#1f8fa0","#2fc8c0","#6ff5dc"],img)
    put(img,line((9,9),(9,14),0.5)|line((6.5,11.5),(11.5,11.5),0.5),"#ffffff")
    return img
def sun():
    img=Image.new("RGBA",(18,18),(0,0,0,0)); rays=set()
    for k in range(8):
        a=math.radians(45*k); rays|=line((9+5.2*math.cos(a),9+5.2*math.sin(a)),(9+8.2*math.cos(a),9+8.2*math.sin(a)),0.6)
    put(img,rays,"#ffc23c")
    shaded(circle(9,9,4.6),["#9c2a0e","#d6501a","#f28a22","#ffc23c","#fff0a0"],img)
    return img
def venom_drops():
    img=Image.new("RGBA",(18,18),(0,0,0,0)); ramp=["#1f0a2e","#4a1a6e","#7a2fa8","#a95bd6","#d7a3f2"]
    for cx,cy in ((4.6,14.4),(13.4,14.4),(9,8)):          # three venom drops stacked in a pyramid
        shaded(drop(cx,cy,2.9,2.4),ramp,img)
    put(img,{(9,8),(4,14),(13,14)},"#b6f23c")
    return img

ICE=["#0a2a55","#2a6fb0","#5aa8e0","#9ae7ff","#e8faff"]
EFFECTS={   # id: (A, B, C)
 "freeze":(lambda: recolour(vanilla("slowness"),ICE), snowflake, lambda: shard("ice_star")),
 "leech_seed":(lambda: recolour(vanilla("regeneration"),["#14380c","#2f7a1e","#5fb83a","#8fdc5a","#d8f7a0"]), seed, lambda: shard("overgrown_prosperity")),
 "bleed":(lambda: recolour(vanilla("poison"),["#2e0308","#5e0a16","#8a1020","#c42030","#ff5a5a"]), blood_drop, lambda: shard("crimson_sanguis")),
 "blitz_rush":(lambda: recolour(vanilla("speed"),["#5a3d04","#b8860b","#e8b820","#f6d040","#9ff0ff"]), bolt, lambda: shard("storm_fang")),
 "tidecaller":(lambda: recolour(vanilla("water_breathing"),["#1f8fa0","#2fd8c8","#6ff5dc","#b8fff2","#ffffff"]), tide_drop, lambda: shard("cerulean_abyss")),
 "radiance":(lambda: recolour(vanilla("glowing"),["#5a2a06","#d6501a","#f28a22","#ffc23c","#fff0a0"]), sun, lambda: shard("cruel_sun")),
 "venom_stack":(lambda: recolour(vanilla("poison"),["#1f0a2e","#4a1a6e","#7a2fa8","#a95bd6","#d7a3f2"]), venom_drops, lambda: shard("venom_crown")),
}
# the user's picks (1 = vanilla recolour, 2 = symbol, 3 = family shard)
FINAL={"bleed":1,"blitz_rush":1,"freeze":2,"leech_seed":2,"radiance":1,"tidecaller":2,"venom_stack":1}

if __name__=="__main__":
    # default: the final icons as <id>.png; "candidates [out_dir]" writes all three per effect as <id>_temp_<n>.png
    if len(sys.argv)>1 and sys.argv[1]=="candidates":
        out=sys.argv[2] if len(sys.argv)>2 else OUT; os.makedirs(out,exist_ok=True)
        for eid,fs in EFFECTS.items():
            for i,f in enumerate(fs,1):
                f().save(f"{out}/{eid}_temp_{i}.png")
    else:
        for eid,k in FINAL.items():
            EFFECTS[eid][k-1]().save(f"{OUT}/{eid}.png")
