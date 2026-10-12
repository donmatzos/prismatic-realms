import glob, io, os, zipfile
from PIL import Image
# Crystal Pouch textures.
#  gui(): the screen background, built from vanilla's generic_54 (chest) texture: the top part for two rows, the slot frames
#         of columns 0 and 8 painted over with the panel colour so only the 7 middle columns (x = 26 + 18 * c) remain,
#         then the player inventory part.
#  silhouette(item_png): the empty-slot hint, a dark grey outline of the item (the shape's edge pixels only).
GREY_OUTLINE=(55,55,55,255)
PANEL=(198,198,198,255)

def jar_image(path):
    jar=sorted(glob.glob(os.path.expanduser("~/.gradle/caches/fabric-loom/*/minecraft-client.jar")))[-1]
    with zipfile.ZipFile(jar) as z:
        return Image.open(io.BytesIO(z.read(path))).convert("RGBA")

def gui():
    src=jar_image("assets/minecraft/textures/gui/container/generic_54.png")
    out=Image.new("RGBA",(256,256),(0,0,0,0))
    top=src.crop((0,0,176,17+2*18)); out.paste(top,(0,0))
    for row in range(2):
        for col in (0,8):
            x,y=7+col*18,17+row*18
            for yy in range(y,y+18):
                for xx in range(x,x+18):
                    out.putpixel((xx,yy),PANEL)
    out.paste(src.crop((0,126,176,222)),(0,17+2*18))
    return out

def silhouette(item_png):
    im=Image.open(item_png).convert("RGBA"); px=im.load(); w,h=im.size
    out=Image.new("RGBA",(w,h),(0,0,0,0)); po=out.load()
    solid=lambda x,y: 0<=x<w and 0<=y<h and px[x,y][3]>0
    for y in range(h):
        for x in range(w):
            if solid(x,y) and any(not solid(x+i,y+j) for i,j in ((1,0),(-1,0),(0,1),(0,-1))):
                po[x,y]=GREY_OUTLINE
    return out

# Icons: vanilla templates recoloured by brightness into the Spectral Binder's palette (pale lavender greys), with
# per-candidate details.
BINDER=["#3b3550","#6f6890","#a49cc4","#d4cdee","#f4f0ff"]          # dark -> light lavender greys
RAINBOW=["#e5414f","#f28a22","#f6d040","#5fb83a","#5aa8e0","#3346a8","#a95bd6"]
def hexc(h): return tuple(int(h[i:i+2],16) for i in (1,3,5))+(255,)

def recolour(img,ramp):
    out=img.copy(); p=out.load(); R=[hexc(c) for c in ramp]
    lums=[0.299*r+0.587*g+0.114*b for x in range(16) for y in range(16) for r,g,b,a in [img.getpixel((x,y))] if a]
    lo,hi=min(lums),max(lums)
    for y in range(16):
        for x in range(16):
            r,g,b,a=p[x,y]
            if a:
                t=(0.299*r+0.587*g+0.114*b-lo)/max(1,hi-lo)
                p[x,y]=R[min(len(R)-1,int(t*len(R)))][:3]+(a,)
    return out

def widest_row(img,y_from,y_to):
    """the row (within the range) with the most opaque inner pixels and their x positions, for placing a band
    (the outline colour, the darkest ramp step, is skipped so accents never land on the outline)"""
    ok=lambda x,y: img.getpixel((x,y))[3]>0 and img.getpixel((x,y))[:3]!=hexc(BINDER[0])[:3]
    best=max(range(y_from,y_to),key=lambda y:sum(ok(x,y) for x in range(16)))
    return best,[x for x in range(16) if ok(x,best)]

def pouch_candidates():
    base=jar_image("assets/minecraft/textures/item/bundle.png")
    plain=recolour(base,BINDER)
    banded=plain.copy(); y,xs=widest_row(banded,9,13)        # 2: a rainbow band across the bag's belly
    inner=xs[1:]                                             # the first pixel is the bag's lighter outer edge
    for i,x in enumerate(inner): banded.putpixel((x,y),hexc(RAINBOW[i*len(RAINBOW)//max(1,len(inner))]))
    gem=plain.copy(); y,xs=widest_row(gem,4,8)               # 3: a binder gem clasp at the neck (centre of the tie row)
    cx=xs[len(xs)//2]                                        # 2x2 gem: white facets with a blue and a violet corner
    for dx,dy,c in ((0,0,"#ffffff"),(1,0,"#5aa8e0"),(0,1,"#a95bd6"),(1,1,"#ffffff")): gem.putpixel((cx+dx,y+dy),hexc(c))
    return {"plain":plain,"rainbow_band":banded,"gem_clasp":gem}

def template_candidates():
    base=jar_image("assets/minecraft/textures/item/netherite_upgrade_smithing_template.png")
    plain=recolour(base,BINDER)
    dots=plain.copy(); dark=hexc(BINDER[0])[:3]
    opaque=[(x,y) for y in range(1,15) for x in range(1,15) if plain.getpixel((x,y))[3] and plain.getpixel((x,y))[:3]!=dark]
    for i,(x,y) in enumerate(sorted(opaque,key=lambda p:(p[1],p[0]))[::max(1,len(opaque)//7)][:7]):   # 2: seven rainbow sparks
        dots.putpixel((x,y),hexc(RAINBOW[i]))
    edge=plain.copy()                                           # 3: rainbow along the bottom edge
    y,xs=widest_row(edge,11,15)
    for i,x in enumerate(xs): edge.putpixel((x,y),hexc(RAINBOW[i*len(RAINBOW)//max(1,len(xs))]))
    return {"plain":plain,"rainbow_sparks":dots,"rainbow_edge":edge}

# the user's picks: pouch version 3, template version 2
FINAL_POUCH="gem_clasp"
FINAL_TEMPLATE="rainbow_sparks"
