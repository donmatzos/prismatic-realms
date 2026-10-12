import json,sys; sys.path.insert(0,"tools/textures")
import dowsing_rod_gen as d
from px2png import write
# run from the project root:
#   python3 tools/textures/render_dowsing_rod.py candidates   -> textures/item/dowsing_rod_temp_<1|2|3>.png
#   python3 tools/textures/render_dowsing_rod.py <name>       -> textures/item/dowsing_rod.png from CANDIDATES[name] (+ spec)
out="src/main/resources/assets/prismaticrealms/textures/item"
arg=sys.argv[1]
if arg=="candidates":
    for i,rows in enumerate(d.CANDIDATES.values(),1):
        write({"palette":d.PAL,"pixels":rows},f"{out}/dowsing_rod_temp_{i}.png")
else:
    spec={"palette":d.PAL,"pixels":d.CANDIDATES[arg]}
    json.dump(spec,open("tools/textures/specs/dowsing_rod.json","w"),indent=1)
    write(spec,f"{out}/dowsing_rod.png")
