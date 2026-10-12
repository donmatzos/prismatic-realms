import json,sys; sys.path.insert(0,"tools/textures"); from blood_gen import wounded,shard_tip,PAL
# run from the project root
def dump(path,rows): json.dump({"palette":PAL,"pixels":rows},open(path,"w"),indent=1)
dump("tools/textures/specs/crimson_sanguis_crystal.json",wounded())
dump("tools/textures/specs/crimson_sanguis_shard.json",shard_tip())
