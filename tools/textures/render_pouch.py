import os,sys; sys.path.insert(0,"tools/textures")
import pouch_gen as g
# run from the project root: python3 tools/textures/render_pouch.py gui
A="src/main/resources/assets/prismaticrealms/textures"
FAMILIES=["crimson_sanguis","cruel_sun","storm_fang","overgrown_prosperity","ice_star","cerulean_abyss","venom_crown"]
if sys.argv[1]=="gui":
    os.makedirs(f"{A}/gui/container",exist_ok=True); os.makedirs(f"{A}/gui/sprites/container/crystal_pouch",exist_ok=True)
    g.gui().save(f"{A}/gui/container/crystal_pouch.png")
    for f in FAMILIES:
        for kind in ("shard","crystal"):
            g.silhouette(f"{A}/item/{f}_{kind}.png").save(f"{A}/gui/sprites/container/crystal_pouch/{f}_{kind}.png")
elif sys.argv[1]=="candidates":
    for name,cands in (("crystal_pouch",g.pouch_candidates()),("crystal_pouch_upgrade",g.template_candidates())):
        for i,img in enumerate(cands.values(),1):
            img.save(f"{A}/item/{name}_temp_{i}.png")
elif sys.argv[1]=="final":   # python3 tools/textures/render_pouch.py final <pouch candidate> <template candidate>
    g.pouch_candidates()[sys.argv[2]].save(f"{A}/item/crystal_pouch.png")
    g.template_candidates()[sys.argv[3]].save(f"{A}/item/crystal_pouch_upgrade.png")
