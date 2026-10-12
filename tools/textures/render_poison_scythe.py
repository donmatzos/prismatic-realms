import json,sys; sys.path.insert(0,"tools/textures")
import poison_scythe_gen as d
from px2png import write
# run from the project root: python3 tools/textures/render_poison_scythe.py [out_dir] [scale]
# renders the Scorpion Tail icon (poison_scythe_gen.FINAL) and its 32px in-hand texture (re-drawn long blade)
out=sys.argv[1] if len(sys.argv)>1 else "src/main/resources/assets/prismaticrealms/textures/item"; scale=int(sys.argv[2]) if len(sys.argv)>2 else 1
for name,rows in (("scorpion_tail",d.FINAL()),("scorpion_tail_in_hand",d.in_hand())):
    spec={"palette":d.PAL,"pixels":rows}
    if scale==1: json.dump(spec,open(f"tools/textures/specs/{name}.json","w"),indent=1)
    write(spec,f"{out}/{name}.png",scale)
