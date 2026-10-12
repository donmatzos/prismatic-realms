import json,sys; sys.path.insert(0,"tools/textures")
# NOTE: the *_in_hand texture written here is the SHORT-blade base. The game uses the reaper-length blade from
# long_blade.py: copy the new short in-hand into unused-item-sprites/short-blade-in-hand/, then run long_blade.py on it.
import lightning_scythe_gen as d
from px2png import write
# run from the project root: python3 tools/textures/render_lightning_scythe.py [out_dir] [scale]
# renders the Blitz Slasher icon (lightning_scythe_gen.FINAL) and its 32px in-hand texture
out=sys.argv[1] if len(sys.argv)>1 else "src/main/resources/assets/prismaticrealms/textures/item"; scale=int(sys.argv[2]) if len(sys.argv)>2 else 1
icon=d.FINAL()
for name,rows in (("blitz_slasher",icon),("blitz_slasher_in_hand",d.in_hand(icon))):
    spec={"palette":d.PAL,"pixels":rows}
    if scale==1: json.dump(spec,open(f"tools/textures/specs/{name}.json","w"),indent=1)
    write(spec,f"{out}/{name}.png",scale)
