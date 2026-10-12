# Dowsing Rod icon candidates (16x16, held diagonally: grip bottom-left, working end top-right).
# Wood from vanilla's stick, an iron fitting (the recipe's iron ingot), a pale lavender Spectral Binder gem.
PAL={".":None,
     "o":"#28190c",                                  # outline
     "d":"#4e3418","w":"#6b4a24","W":"#8b6438",      # wood: dark, mid, light
     "I":"#5a5a5a","i":"#a0a0a0","j":"#d8d8d8",      # iron: dark, mid, light
     "g":"#9d86e0","G":"#d7cbff","s":"#ffffff"}      # binder gem: body, light, glint

FORKED=[            # 1 = classic forked dowsing rod: two wooden prongs meet at an iron band, the gem set at the fork
    "............oo..",
    "...........oWo..",
    "..........oWo...",
    "...oo....oWo....",
    "..oWWo..oWo.....",
    "...oWWooWo......",
    "....oWGgo.......",
    ".....oGsgo......",
    "......ogIjo.....",
    ".....oIjio......",
    "....owWo........",
    "...owWo.........",
    "..owWo..........",
    ".owWo...........",
    ".odo............",
    "..o............."]
PENDULUM=[          # 2 = pendulum: a short wooden grip, an iron chain and a hanging gem
    ".........oo.....",
    "........oWWo....",
    ".......oWWo.....",
    "......oWWo......",
    ".....oIjo.......",
    "......i.........",
    "......i.........",
    "......i.........",
    "......i.........",
    ".....ogo........",
    "....oGsgo.......",
    "....oGggo.......",
    ".....ogo........",
    "......o.........",
    "................",
    "................"]
WAND=[              # 3 = crystal wand: a straight wooden rod with an iron band, the gem crowning its tip, a small sparkle
    "............s...",
    "..........ooo.s.",
    ".........oGsgo..",
    "........oGggo...",
    "........ogIo....",
    ".......oIjo.....",
    "......oWwo......",
    ".....oWwo.......",
    "....oWwo........",
    "...oWwo.........",
    "..oWwo..........",
    ".oWwo...........",
    ".odo............",
    "..o.............",
    "................",
    "................"]
CANDIDATES={"forked":FORKED,"pendulum":PENDULUM,"wand":WAND}
FINAL="forked"   # picked by the user (version 1)
