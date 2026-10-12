import sys, zlib, struct, json
def write(spec,out,s=1):
    pal={k:(None if v is None else tuple(int(v[i:i+2],16) for i in (1,3,5))+((int(v[7:9],16),) if len(v)>7 else (255,))) for k,v in spec["palette"].items()}
    rows=spec["pixels"]; assert all(len(r)==len(rows[0]) for r in rows),[len(r) for r in rows]
    raw=b""
    for r in rows:
        line=b"".join(bytes(pal[c] or (0,0,0,0))*s for c in r)
        raw+=(b"\0"+line)*s
    w,h=len(rows[0])*s,len(rows)*s
    def chunk(t,d): return struct.pack(">I",len(d))+t+d+struct.pack(">I",zlib.crc32(t+d))
    open(out,"wb").write(b"\x89PNG\r\n\x1a\n"+chunk(b"IHDR",struct.pack(">IIBBBBB",w,h,8,6,0,0,0))+chunk(b"IDAT",zlib.compress(raw,9))+chunk(b"IEND",b""))
if __name__=="__main__":
    # usage: px2png.py spec.json out.png [scale]
    write(json.load(open(sys.argv[1])),sys.argv[2],int(sys.argv[3]) if len(sys.argv)>3 else 1)
