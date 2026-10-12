import glob, io, json, math, os, zipfile
from PIL import Image
from bolt_gen import bevel, seg  # facet shading (o/d/m/l/h/w ramp keys)
# Crystal ore textures: a family motif (palette + 16 char rows, '.' = untouched) composed onto vanilla stone/deepslate.
# Tint keys (lowercase 't' + digit) stain the base pixel instead of replacing it.

def vanilla_texture(name):
    # read textures/block/<name>.png straight from the Minecraft client jar in the Gradle cache
    jar=sorted(glob.glob(os.path.expanduser("~/.gradle/caches/fabric-loom/*/minecraft-client.jar")))[-1]
    with zipfile.ZipFile(jar) as z:
        return Image.open(io.BytesIO(z.read(f"assets/minecraft/textures/block/{name}.png"))).convert("RGBA")

def hexrgb(h): return tuple(int(h[i:i+2],16) for i in (1,3,5))

def compose(spec):
    img=vanilla_texture(spec["base"]); px=img.load()
    pal=spec["palette"]; tints=spec.get("tints",{})
    for y,row in enumerate(spec["pixels"]):
        for x,c in enumerate(row):
            if c=='.': continue
            if c in tints:
                col,k=hexrgb(tints[c]["color"]),tints[c]["strength"]; r,g,b,a=px[x,y]
                px[x,y]=(round(r+(col[0]-r)*k),round(g+(col[1]-g)*k),round(b+(col[2]-b)*k),255)
            else:
                px[x,y]=hexrgb(pal[c])+(255,)
    return img

def blank(): return [['.']*16 for _ in range(16)]
def stamp(g,part):
    for y in range(16):
        for x in range(16):
            if part[y][x]!='.': g[y][x]=part[y][x]
def outline(g,skip=''):
    # dark rim around the motif (not around tint pixels), so the crystals read as embedded objects, unlike vanilla specks
    out=[r[:] for r in g]
    for y in range(16):
        for x in range(16):
            if g[y][x]=='.' and any(0<=y+j<16 and 0<=x+i<16 and g[y+j][x+i] not in '.' and g[y+j][x+i] not in skip for i,j in((1,0),(-1,0),(0,1),(0,-1))):
                out[y][x]='o'
    return out
BRIGHTER={'d':'m','m':'l','l':'h','h':'h','w':'w'}
def spike(pts,deep=0.8,spine=9,bright=1):
    # small bevelled crystal; tiny shapes are mostly edge pixels, so lift the shading by `bright` steps to keep them readable
    g=bevel(pts,deep=deep,spine_d=spine)
    for _ in range(bright):
        g=[[BRIGHTER.get(c,c) for c in r] for r in g]
    return g
def rows(g): return [''.join(r) for r in g]
def pair(name,palette,pixels,tints=None):
    # the same motif on both vanilla bases
    for base,prefix in (("stone",""),("deepslate","deepslate_")):
        yield prefix+name,{"base":base,"palette":palette,"tints":tints or {},"pixels":pixels}

def save(spec,path): compose(spec).save(path)
