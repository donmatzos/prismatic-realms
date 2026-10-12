import json,sys; sys.path.insert(0,"tools/textures"); from sun_gen import molten,shard_chunk,PAL
# run from the project root
def dump(name,rows): json.dump({"palette":PAL,"pixels":rows},open(f"tools/textures/specs/{name}.json","w"),indent=1)
dump("cruel_sun_crystal",molten())
dump("cruel_sun_shard",shard_chunk())
