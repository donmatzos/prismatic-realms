import glob, io, math, os, zipfile
from PIL import Image
# Scythes: one shared shape, recoloured per material from the matching vanilla hoe's palette.
# Colour roles: A shadow/outline, B/D/E handle (dark/mid/light), C blade rim, F/G/H blade ramp (dark/mid/bright).
MATERIALS=["wooden","stone","copper","golden","iron","diamond","netherite"]
# pixel positions in every vanilla hoe texture that hold each role's colour
SAMPLE={"A":(4,13),"B":(2,13),"D":(3,12),"E":(3,13),"C":(11,3),"F":(9,3),"G":(8,2),"H":(7,2)}

def hoe_palette(material):
    jar=sorted(glob.glob(os.path.expanduser("~/.gradle/caches/fabric-loom/*/minecraft-client.jar")))[-1]
    with zipfile.ZipFile(jar) as z:
        im=Image.open(io.BytesIO(z.read(f"assets/minecraft/textures/item/{material}_hoe.png"))).convert("RGBA")
    px=im.load()
    return {".":None,**{k:"#%02x%02x%02x"%px[x,y][:3] for k,(x,y) in SAMPLE.items()}}

def shaft_row(size,y):
    # the vanilla hoe's shaft construction: dark left edge B, alternating D/E body, shadow A below-right
    row=['.']*size; x=size-1-y
    if 0<=x<size: row[x]='B'
    if 0<=x+1<size: row[x+1]='D' if y%2 else 'E'
    if 0<=x+2<size: row[x+2]='A'
    return row

def build(head,size,width=16):
    """head: hand-drawn rows 0..8 of the 16px icon (blade plus the top of the shaft).
    The icon is head + shaft down to the end cap; the 32px in-hand texture puts the same head (same pixel size)
    in its top-right corner and runs a twice-as-long shaft to the bottom-left, like vanilla's spear_in_hand."""
    off=size-width
    rows=[list('.'*off+r) for r in head]
    for y in range(len(head),size-2):
        rows.append(shaft_row(size,y))
    cap=['.']*size; cap[2]='A'; cap[3]='A'
    rows.append(cap); rows.append(['.']*size)
    assert all(len(r)==size for r in rows) and len(rows)==size,(size,[len(r) for r in rows])
    return [''.join(r) for r in rows]

def icon_and_inhand(head):
    assert all(len(r)==16 for r in head),[len(r) for r in head]
    return build(head,16),build(head,32)

ARCHED_ICON=[
    "......CCCCC.....",
    "....CCGGGGGCC...",
    "..CCGGHHHHHAAC..",
    ".CGGHAAAAA..BD..",
    "CGHA.......BEA..",
    "CGA.......BDA...",
    "CHA......BEA....",
    ".CA.....BDA.....",
    ".A.....BEA......"]
# in-hand: the same arched blade drawn 4px longer with a deeper tip (user request); same pixel size, 20px wide head
ARCHED_IN_HAND=[
    ".........CCCCC......",
    "......CCCGGGGGCC....",
    "...CCCGGGHHHHHAAC...",
    "..CGGGHHAAAAA...BD..",
    ".CGHHA.........BEA..",
    "CGHA..........BDA...",
    "CGHA.........BEA....",
    "CGA.........BDA.....",
    "CHA........BEA......",
    ".CA.......BDA.......",
    ".A.......BEA........"]
def arched():
    # crescent blade whose back arches up over the shaft before sweeping down into the tip; honed edge (H) on the inside
    assert all(len(r)==16 for r in ARCHED_ICON) and all(len(r)==20 for r in ARCHED_IN_HAND)
    return build(ARCHED_ICON,16),build(ARCHED_IN_HAND,32,20)
def long_arched():
    """icon: the arched blade; in-hand (user, 2026-10-12): the reaper-length crescent shared with the unique scythes
    (long_blade.crescent), in palette keys: shadow rim A, then C, F, G, H from the back to the bright cutting edge H,
    one step darker towards the tip; the shaft and end cap as before."""
    from long_blade import crescent, layer
    icon,hand=arched()
    rows=[list(r) for r in hand]
    for y in range(12):
        for x in range(32):
            if rows[y][x]!='.' and not (y>=3 and 31-y<=x<=33-y): rows[y][x]='.'
    depth,edges=crescent()
    for (x,y),dd in depth.items():
        if rows[y][x]!='.': continue
        rows[y][x]='A' if dd==0 else 'H' if (x,y) in edges else "CFGH"[layer(x,dd,4)]
    return icon,[''.join(r) for r in rows]
NETHERITE_SWORD={"C":"#4a2940","A":"#231012","H":"#867b86"}   # purple-tinted back edge, near-black shadow, bright edge
NETHERITE_RAMP=["#4f3c3e","#51444e","#5d565d","#706770"]        # blade body: dark at the root, light towards the tip
def detail(material,rows,palette,progress=None):
    """per-material finishing. Netherite copies the netherite sword: purple-tinted back edge, near-black shadow, and a body
    gradient from dark at the blade's root to light at its tip (progress: {(x,y): 0..1 along the blade}; without it the
    columns are used, the blade's root being on the right next to the shaft)."""
    if material!="netherite":
        return rows,palette
    palette={**palette,**NETHERITE_SWORD,**dict(zip("pqrs",NETHERITE_RAMP))}
    out=[]
    for y,row in enumerate(rows):
        r=[c for c in row]
        for x,c in enumerate(r):
            if c in 'FG':
                # blade root on the right (next to the shaft), tip on the left: icon root x=13 over 12px, in-hand root x=28 over 28px (the long blade)
                root,span=(13,12) if len(row)==16 else (28,28)
                t=progress.get((x,y),0.0) if progress else min(1.0,max(0.0,(root-x)/span+y*0.05))
                k=min(3,int(t*4)) - (1 if c=='F' and t*4>=1 else 0)
                r[x]="pqrs"[max(0,k)]
        out.append(''.join(r))
    return out,palette
FINAL=long_arched
