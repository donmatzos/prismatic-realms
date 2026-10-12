import json,sys; sys.path.insert(0,"tools/textures"); from poison_gen import crown_b,shard_facet,PAL
# run from the project root
def dump(path,rows): json.dump({"palette":PAL,"pixels":rows},open(path,"w"),indent=1)
dump("tools/textures/specs/venom_crown_crystal.json",crown_b())
dump("tools/textures/specs/venom_crown_shard.json",shard_facet())
