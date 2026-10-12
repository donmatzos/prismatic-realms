import sys
from PIL import Image
sys.path.insert(0,"tools/textures")
from long_blade import crescent, rgb
# Scythe of the Frozen Expanse in-hand rework (user: the long blade lost the icon's features and is "too blue, it should be
# more light blue - icy colors - and keep the features"). The icon stays. Base: the short-blade original (shaft and the
# head that curls around the shaft top are kept); the long crescent (long_blade.crescent) is painted per pixel with the
# icon's features: pale ice body, a thin dark rim only on the back, cream/gold frost patches along the middle, a
# bright blue tip, and a white-gold glow at the root like the icon's.
# run from the project root: python3 tools/textures/ice_long_gen.py [candidates [out_dir]]
BASE="unused-item-sprites/short-blade-in-hand/scythe_of_the_frozen_expanse_in_hand.png"
T="src/main/resources/assets/prismaticrealms/textures/item"
C={"navy":"#042047","deep":"#042759","blue":"#074aae","bright":"#0597eb","steel":"#5a7ebd","sky":"#78a9e8",
   "periwinkle":"#80b1ed","teal":"#7cc3d9","ice":"#9ae7ff","ice2":"#9ce6fe","frost":"#c8f4ff","gold":"#f5d894",
   "cream":"#fffac2","white":"#fffdef"}
KEEP=(24,1,31,9)

def base_image():
    bp=Image.open(BASE).convert("RGBA").load()
    img=Image.new("RGBA",(32,32),(0,0,0,0)); p=img.load()
    for y in range(32):
        for x in range(32):
            if bp[x,y][3] and ((abs(x-(32-y))<=1 and y>=2) or (KEEP[0]<=x<=KEEP[2] and KEEP[1]<=y<=KEEP[3]) or y>=12):
                p[x,y]=bp[x,y]
    return img

def paint(colour):
    img=base_image(); p=img.load(); depth,edges=crescent()
    for (x,y),dd in depth.items():
        if not p[x,y][3]: p[x,y]=rgb(C[colour(x,y,dd,(x,y) in edges)])
    return img

PATCHES=((9,12),(17,20))          # x ranges of the frost patches along the blade's middle (gold left, cream right)
def icon_true(x,y,dd,edge):
    # closest to the icon: pale ice body under a thin navy back, cream/gold frost patches, bright blue tip, glow at the root
    if x<=4: return "blue" if dd==0 or edge else "bright"
    if dd==0: return "navy" if x>=8 else "blue"
    if edge: return "teal" if x%3 else "periwinkle"
    for a,b in PATCHES:
        if a<=x<=b and dd in (2,3): return "gold" if x<(a+b)/2 else "cream"
    if x>=21 and dd>=3: return "white" if dd==3 else "cream"
    return ("ice","sky","ice2","ice","frost")[min(dd-1,4)]
def frost_white(x,y,dd,edge):
    # the lightest (picked): soft steel-blue back instead of navy, frost-white body, one gold/cream frost patch near the root,
    # sky-blue edge, blue tip
    if x<=4: return "blue" if dd==0 else "bright"
    if dd==0: return "steel"
    if edge: return "sky"
    a,b=PATCHES[1]                    # only the frost patch near the root (user: remove the trailing dots at the top)
    if a<=x<=b and dd==3: return "gold" if x<(a+b)/2 else "cream"
    return ("ice","frost","frost","ice2","frost")[min(dd-1,4)]
def glacial_edge(x,y,dd,edge):
    # pale ice body with one continuous cream frost band along the back, fading to gold near the tip, and a bright blue
    # cutting edge like the icon's tip running the whole length
    if x<=3: return "blue" if dd==0 else "bright"
    if dd==0: return "navy" if x>=8 else "blue"
    if edge: return "bright"
    if dd==2: return "cream" if x>=13 else "gold"
    return ("ice","sky","ice","ice2","frost")[min(dd-1,4)]
CANDIDATES=(icon_true,frost_white,glacial_edge)

FINAL=frost_white

if __name__=="__main__":
    # default: the game texture from FINAL; "candidates" writes all three as _temp_<n>
    if len(sys.argv)>1 and sys.argv[1]=="candidates":
        out=sys.argv[2] if len(sys.argv)>2 else T
        for i,f in enumerate(CANDIDATES,1):
            paint(f).save(f"{out}/scythe_of_the_frozen_expanse_in_hand_temp_{i}.png")
    else:
        paint(FINAL).save(f"{T}/scythe_of_the_frozen_expanse_in_hand.png")
