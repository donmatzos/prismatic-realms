import sys
# NOTE: the *_in_hand texture written here is the SHORT-blade base. The game uses the reaper-length blade from
# long_blade.py: copy the new short in-hand into unused-item-sprites/short-blade-in-hand/, then run long_blade.py on it.
from PIL import Image
# Unique scythes drawn by the user as 16px icons (the Scythe of the Frozen Expanse, the Overgrown Reaper): the icon is used
# as-is, and the 32px in-hand texture is built from it at the same pixel size:
#  - the blade's sweep (icon rows 0-5, columns 0-10) is stretched 6px to the left by repeating its own columns, so the
#    blade gets longer and flatter while keeping the drawn colour bands;
#  - the rest of the head (rows 0-8: where the blade meets the shaft, and the top of the shaft) stays as drawn;
#  - the shaft (centre x = 16 - y on the icon) runs on to the bottom-left corner, cycling through the icon's own shaft rows
#    9-14 (left outline, body, right outline), and ends with the icon's end cap (row 15).
# run from the project root: python3 tools/textures/sprite_scythe_in_hand.py <icon.png> <out_icon.png> <out_in_hand.png>
src,out_icon,out_hand=sys.argv[1:4]
icon=Image.open(src).convert("RGBA"); assert icon.size==(16,16)
icon.save(out_icon)
px=icon.load()
hand=Image.new("RGBA",(32,32),(0,0,0,0)); hp=hand.load()
HEAD_ROWS,SWEEP_ROWS,SWEEP_COLS,EXTRA=9,6,11,6
for y in range(HEAD_ROWS):
    for x in range(16):
        if y<SWEEP_ROWS and x<SWEEP_COLS: continue
        hp[x+16,y]=px[x,y]
    if y<SWEEP_ROWS:
        width=SWEEP_COLS+EXTRA
        for tx in range(width):
            hp[16-EXTRA+tx,y]=px[tx*SWEEP_COLS//width,y]
shaft=[(px[16-y-1,y],px[16-y,y],px[16-y+1,y]) for y in range(9,15)]   # (left outline, body, right outline)
for y in range(HEAD_ROWS,30):
    c=32-y; left,body,right=shaft[(y-HEAD_ROWS)%len(shaft)]
    hp[c-1,y]=left; hp[c,y]=body; hp[c+1,y]=right
for dx in range(-2,3):            # end cap: the icon's row 15 around the last shaft centre (icon x = 2)
    if 0<=2+dx<16 and px[2+dx,15][3]: hp[3+dx,30]=px[2+dx,15]
hand.save(out_hand)
