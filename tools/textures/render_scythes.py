import json,sys; sys.path.insert(0,"tools/textures")
from scythe_gen import MATERIALS,hoe_palette
import scythe_gen
# run from the project root: python3 tools/textures/render_scythes.py [final|candidates] [out_dir]
# final (default): every material with scythe_gen.FINAL; candidates: shape candidates in iron as iron_scythe(_in_hand)_temp_N
# (needs a CANDIDATES list in scythe_gen, e.g. when reworking the shape)
# icons: only the 16px icons
mode=sys.argv[1] if len(sys.argv)>1 else "final"; out=sys.argv[2] if len(sys.argv)>2 else "src/main/resources/assets/prismaticrealms/textures/item"
sys.path.insert(0,"tools/textures"); from px2png import write as _w
def dump(name,rows,pal):
    spec={"palette":pal,"pixels":rows}
    json.dump(spec,open(f"tools/textures/specs/{name}.json","w"),indent=1); _w(spec,f"{out}/{name}.png")
if mode=="icons":
    for m in MATERIALS:
        icon,_=scythe_gen.FINAL(); icon,pal=scythe_gen.detail(m,icon,hoe_palette(m)); dump(f"{m}_scythe",icon,pal)
elif mode=="candidates":
    pal=hoe_palette("iron")
    for k,f in enumerate(scythe_gen.CANDIDATES,1):
        icon,hand=f(); dump(f"iron_scythe_temp_{k}",icon,pal); dump(f"iron_scythe_in_hand_temp_{k}",hand,pal)
else:
    for m in MATERIALS:
        icon,hand=scythe_gen.FINAL(); pal=hoe_palette(m)
        icon,ipal=scythe_gen.detail(m,icon,pal); hand,hpal=scythe_gen.detail(m,hand,pal)
        dump(f"{m}_scythe",icon,ipal); dump(f"{m}_scythe_in_hand",hand,hpal)
