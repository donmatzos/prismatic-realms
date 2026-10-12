import json,sys; sys.path.insert(0,"tools/textures")
import ocean_scythe_gen as d
from px2png import write
# run from the project root: python3 tools/textures/render_ocean_scythe.py [candidates]
# renders The Shallow Waters icon (ocean_scythe_gen.FINAL, deep-blue rework), its in-hand (LONG_FINAL) and, with "candidates", the long in-hand
# candidates as the_shallow_waters_in_hand_temp_<n>.png
out="src/main/resources/assets/prismaticrealms/textures/item"
def dump(name,rows):
    spec={"palette":d.DEEP,"pixels":rows}
    json.dump(spec,open(f"tools/textures/specs/{name}.json","w"),indent=1); write(spec,f"{out}/{name}.png")
dump("the_shallow_waters",d.FINAL())
dump("the_shallow_waters_in_hand",d.LONG_FINAL())
if len(sys.argv)>1 and sys.argv[1]=="candidates":
    for i,f in enumerate(d.LONG_CANDIDATES,1):
        write({"palette":d.DEEP,"pixels":f()},f"{out}/the_shallow_waters_in_hand_temp_{i}.png")
