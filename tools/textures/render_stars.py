import json,sys; sys.path.insert(0,'tools/textures'); from star_gen import star,PAL
V={
 "ice_star_crystal": star(8.0,8.7,1.9,7.6,1.9,0.38),
}
for k,rows in V.items():
    json.dump({"palette":PAL,"pixels":rows},open(f"tools/textures/specs/{k}.json","w"),indent=1)
    print(k); print('\n'.join(rows))
