import json,sys; sys.path.insert(0,"tools/textures"); from nature_gen import overgrown_a,shard_corner,seed,leafgem2,PAL
# run from the project root
def dump(path,rows): json.dump({"palette":PAL,"pixels":rows},open(path,"w"),indent=1)
dump("tools/textures/specs/overgrown_prosperity_crystal.json",overgrown_a())
dump("tools/textures/specs/overgrown_prosperity_shard.json",shard_corner())
dump("unused-item-sprites/nature_sprouting_seed.json",seed())
dump("unused-item-sprites/nature_mossy_leaf.json",leafgem2())
