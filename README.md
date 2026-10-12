# Prismatic Realms

A Fabric mod for Minecraft 26.3 themed around the colours of the rainbow. It adds seven crystal families, ores to find them, and a new weapon: the scythe.

## Crystal families

Each colour of the rainbow has its own crystal family with a theme:

| Colour | Family | Theme |
|---|---|---|
| Red | Crimson Sanguis | Blood |
| Orange | Cruel Sun | Sun |
| Yellow | Storm Fang | Lightning |
| Green | Overgrown Prosperity | Nature |
| Blue | Ice Star | Ice |
| Indigo | Cerulean Abyss | Ocean |
| Violet | Venom Crown | Poison |

Every family has a **shard** and a **crystal**. Two more families, **Light** and **Dark**, are planned as the endgame.

- **Spectral Binder:** the colourless "glue" that holds crystals together. Craft 2 from any shard plus a slime ball or honeycomb.
- **Crystals:** 4 shards of one family in a plus shape around a Spectral Binder.

## Crystal ores

Every family has an ore in a stone and a deepslate variant (14 ores in total). They are rare everywhere in the Overworld but much more common in their family's themed biomes and height range:

| Ore | Where to look |
|---|---|
| Crimson Sanguis | badlands and the pale garden, deep underground |
| Cruel Sun | deserts, savannas and the dappled forest, from sea level up |
| Storm Fang | mountain peaks, windswept hills and meadows, high up |
| Overgrown Prosperity | jungles, lush caves, dark and old forests, mid depths |
| Ice Star | snowy and frozen biomes, high to mid |
| Cerulean Abyss | oceans, deep underground |
| Venom Crown | swamps and sulfur caves, low to mid depths |

Mining an ore with a pickaxe drops shards:

- Wooden, stone or copper pickaxe: 1 to 3 shards.
- Golden, iron, diamond or netherite pickaxe: 3 to 5 shards, plus a 5 % chance at a Spectral Binder (25 % with netherite).
- Fortune increases the shard count. Silk Touch drops the ore itself. The ores give experience like diamond ore.

## Dowsing Rod

A tool that finds ores for you. Hold the Dowsing Rod in your main hand and what you're looking for in your off hand, then right-click:

- **A crystal shard, crystal or ore** searches for that family's ore.
- **A vanilla ore or its material** (coal, raw iron, raw copper, raw gold, redstone, lapis, a diamond, an emerald, nether quartz, ancient debris or netherite scrap) searches for that ore, deepslate and nether variants included.

It scans 32 blocks in every direction and tells you how far away the nearest one is, in which direction and how far up or down, while a short trail of sparkles points the way. Each scan costs 2 durability and needs 3 seconds to recharge. With **Efficiency** it also shows the exact coordinates; Unbreaking and Mending work as usual.

Craft it from any shard, an iron ingot and a stick, stacked in one column. Repair it with a Spectral Binder.

## Crystal Pouch

A pouch that keeps your shards and crystals out of the way. Craft it from a bundle and a Spectral Binder.

- **Storage:** one slot per family for shards (up to 512 each) and for crystals (up to 128 each). Right-click the pouch to open it: click with items to put them in, right-click a slot to take out a stack, shift-click to empty a slot into your inventory, or shift-click shards and crystals in your inventory to store them.
- **Auto-collect:** while the pouch is in your inventory, shards and crystals you pick up go straight into it.
- **In any screen:** hover the pouch to see what's inside, scroll to pick an entry and right-click to take a stack out, for example at a crafting table. Click shards or crystals onto it to store them.
- **Upgrades** at the smithing table double the capacity twice: the **Crystal Pouch +1** (Crystal Pouch Upgrade +1 + a diamond: 1,024 shards and 256 crystals per family) and the **Crystal Pouch +2** (Crystal Pouch Upgrade +2 + a netherite ingot: 2,048 and 512). The upgrades keep the contents.
- **Upgrade templates:** a Spectral Binder surrounded by one shard of each family and an amethyst shard (+1), or one crystal of each family and an amethyst block (+2).

## Scythes

Scythes are heavy, slow weapons: they hit harder than a sword of the same material, reach further, and sweep like swords. Sword enchantments work on them. In your hand they show a long reaper blade.

- **Material scythes:** wood, stone, copper, gold, iron and diamond. Craft them from 2 of the material, a Spectral Binder and 2 sticks. The netherite scythe is a smithing upgrade from the diamond one, like other netherite gear.
- **Harvesting:** right-click a fully grown crop with any scythe to harvest it with **double the drops**. It is replanted automatically, and fully grown crops directly next to it (north, south, east and west) are harvested too. Each click costs one durability. Works on wheat, carrots, potatoes, beetroots and nether wart.

### Unique scythes

Each crystal family has its own unique scythe with netherite strength. It is crafted like the material scythes with 2 crystals of its family and repaired with them. A **fully charged hit** triggers a special effect:

| Scythe | Family | Charged-hit effect |
|---|---|---|
| Crimson Diabell | Crimson Sanguis | **Bleed:** the target loses health every second for 5 s, twice as much while it keeps moving. |
| Melting Sunray | Cruel Sun | **Radiance:** you gain Fire Resistance for 6 s, and anything that hurts you catches fire. |
| Blitz Slasher | Storm Fang | **Blitz Rush:** you move 50 % faster for 3 s. |
| Overgrown Reaper | Overgrown Prosperity | **Leech Seed:** drains the target's health for 5 s and heals you by the same amount. |
| Scythe of the Frozen Expanse | Ice Star | **Freeze:** slows the target to a quarter of its speed for 3 s. |
| The Shallow Waters | Cerulean Abyss | **Tidecaller:** Dolphin's Grace and Water Breathing for 8 s; while you're in water, every hit heals you. |
| Scorpion Tail | Venom Crown | **Venom Stack:** poisons the target for 6 s. Each further charged hit adds a stack (up to 5), and more stacks deal more damage. |

Each effect has its own particles and status icon, and the scythe's tooltip describes it.

## Planned

- The **Light** and **Dark** endgame families.

## License

MIT, see [LICENSE](LICENSE).
