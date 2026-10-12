import json,sys; sys.path.insert(0,"tools/textures"); from binder_gen import gem,PAL
# run from the project root
json.dump({"palette":PAL,"pixels":gem()},open("tools/textures/specs/spectral_binder.json","w"),indent=1)
