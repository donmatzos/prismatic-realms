import json,sys; sys.path.insert(0,"tools/textures"); from bolt_gen import bolt_classic,shard_chip,PAL
# run from the project root
def dump(name,rows): json.dump({"palette":PAL,"pixels":rows},open(f"tools/textures/specs/{name}.json","w"),indent=1)
dump("storm_fang_crystal",bolt_classic())
dump("storm_fang_shard",shard_chip())
