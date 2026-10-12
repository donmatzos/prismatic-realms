import json,sys; sys.path.insert(0,"tools/textures"); from abyss_gen import geode,shard_wedge,PAL
# run from the project root
def dump(path,rows): json.dump({"palette":PAL,"pixels":rows},open(path,"w"),indent=1)
dump("tools/textures/specs/cerulean_abyss_crystal.json",geode())
dump("tools/textures/specs/cerulean_abyss_shard.json",shard_wedge())
