import json,sys; sys.path.insert(0,"tools/textures")
from ore_gen import pair,save
# run from the project root: python3 tools/textures/render_ores.py <family> [out_dir]
# a family module defines either CANDIDATES (list of motif functions, rendered as <family>_ore_temp_N) or FINAL (the chosen motif)
fam=sys.argv[1]; out=sys.argv[2] if len(sys.argv)>2 else "src/main/resources/assets/prismaticrealms/textures/block"
mod=__import__(f"ore_{fam}")
jobs=[(f"{fam}_ore",mod.FINAL)] if hasattr(mod,"FINAL") else [(f"{fam}_ore_temp_{k}",f) for k,f in enumerate(mod.CANDIDATES,1)]
for base,f in jobs:
    for name,spec in pair(base,mod.PAL,f(),mod.TINTS):
        json.dump(spec,open(f"tools/textures/specs/{name}.json","w"),indent=1); save(spec,f"{out}/{name}.png")
