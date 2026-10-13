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

It scans 32 blocks in every direction and writes into the chat how far away the nearest one is, in which direction and how far up or down, while a short trail of sparkles points the way. Each scan costs 2 durability and needs 3 seconds to recharge. With **Efficiency** it also shows the exact coordinates; Unbreaking and Mending work as usual.

Craft it from any shard, an iron ingot and a stick, stacked in one column. Repair it with a Spectral Binder.

## Crystal Pouch

A pouch that keeps your shards and crystals out of the way. Craft it from a bundle and a Spectral Binder.

- **Storage:** one slot per family for shards (up to 512 each) and for crystals (up to 128 each). Right-click the pouch to open it: click with items to put them in, right-click a slot to take out a stack, shift-click to empty a slot into your inventory, or shift-click shards and crystals in your inventory to store them.
- **Auto-collect:** while the pouch is in your inventory, shards and crystals you pick up go straight into it.
- **In any screen:** hover the pouch to see what's inside, scroll to pick an entry and right-click to take a stack out, for example at a crafting table. Click shards or crystals onto it to store them.
- **Upgrades** at the smithing table double the capacity twice: the **Crystal Pouch +1** (Crystal Pouch Upgrade +1 + a diamond: 1,024 shards and 256 crystals per family) and the **Crystal Pouch +2** (Crystal Pouch Upgrade +2 + a netherite ingot: 2,048 and 512). The upgrades keep the contents.
- **Upgrade templates:** a Spectral Binder surrounded by one shard of each family and an amethyst shard (+1), or one crystal of each family and an amethyst block (+2).

## Artifacts

Your inventory has an **artifact bar**: three slots on its right side (also on the creative inventory tab). An artifact only works while it sits in one of them, and each artifact can be worn once at a time. Shift-click an artifact to put it on or take it off, and hover the bar's frame to see all your artifact bonuses.

Artifacts give passive bonuses, such as extra armour or speed, ongoing effects, or effects when you're hit, attack, mine or kill. Equipped artifacts drop when you die, like the rest of your inventory (`keepInventory` keeps them).

Artifacts are crafted at the Spectral Altar. More family artifacts are on the way.

### Spectral Altar

The artifact station, built like an enchanting table in the Spectral Binder's colours: dark lavender stone with a rainbow inlay and corner pillars, and a pale top with a rainbow ring. Above it floats a glowing Spectral Binder, circled by one shard of every family in rainbow order. They orbit faster when you come close. It gives off light like an enchanting table and can be mined with any pickaxe.

Craft it like an enchanting table, with a Spectral Binder in place of the book and two crystals (of any family) in place of the diamonds, over four obsidian. The recipe unlocks once you pick up a crystal.

**Crafting artifacts:** right-click the altar. Every artifact uses the same shape, a plus of five slots:

- top: a crystal of the artifact's family
- left and right: one shard of that family each
- centre: a **Spectral Setting** (4 gold ingots around a Spectral Binder, at a crafting table)
- bottom: the artifact's catalyst

The altar also burns **3 shards of any family** as fuel for each artifact, from the fuel slot on the left. Without enough fuel the result is shown greyed out. Shift-clicking shards puts them into the fuel slot first. Anything left in the altar comes back to you when you close it.

### Frostwalker Star (Ice Star)

A star pendant made from an Ice Star crystal, two Ice Star shards and **blue ice** as the catalyst. While you wear it:

- Water under you freezes into frosted ice and lava into **Rimed Magma**, in a radius of 4 blocks, like Frost Walker II. Rimed Magma is a frozen crust you can walk on; it cracks and melts back into lava after a short while, and breaking it leaves lava. The crust under your feet keeps refreezing while you wear the Star, so you can stand still on it safely.
- Magma blocks don't hurt you (campfires, fire and lava still do).
- You don't freeze in powder snow.

### Sunblessed Talisman (Cruel Sun)

A molten sun talisman made from a Cruel Sun crystal, two Cruel Sun shards and a **blaze rod** as the catalyst. While you wear it:

- Blocks you mine drop **smelted**: whatever a furnace would make of each drop (raw iron → iron ingot, ancient debris → netherite scrap, sand → glass, logs → charcoal, cobblestone → stone, …). Fortune still multiplies the drops, and you get the furnace's experience on the spot. Mining with **Silk Touch** turns it off, so you can still collect ore blocks.
- You're **immune to fire**: fire, burning, lava, magma blocks, campfires and fireballs don't hurt you, and flames go out right away. Lava still slows you down.

### Stormfather's Heirloom (Storm Fang)

An old storm pendant made from a Storm Fang crystal, two Storm Fang shards and a **breeze rod** as the catalyst. While you wear it:

- You move **50 % faster**, all the time. The bonus adds up with the Blitz Slasher's Blitz Rush instead of multiplying: both together make you twice as fast, and sprinting comes on top as usual.
- **Storm step:** you walk up single blocks without jumping, like a horse.

### Abyssal Heart (Cerulean Abyss)

A deep-blue heart pendant made from a Cerulean Abyss crystal, two Cerulean Abyss shards and a **nautilus shell** as the catalyst. While you wear it:

- You never run out of air underwater.
- You walk through water as fast as on land, like Depth Strider III.
- You swim fast: Dolphin's Grace while you're in water (it fades shortly after you leave it, and never cuts a Dolphin's Grace potion short).

### Bloodstone Chalice (Crimson Sanguis)

A gold goblet made from a Crimson Sanguis crystal, two Crimson Sanguis shards and a **ghast tear** as the catalyst. While you wear it:

- You have **4 extra hearts**.
- **Blood Pact:** when you would die, the chalice breaks instead and heals you to full health, with the same animation as a Totem of Undying (showing the chalice, in red). The extra hearts go with it. It doesn't save you from the void or `/kill`, and a Totem of Undying in your hand is used first.

## Scythes

Scythes are heavy, slow weapons: they hit harder than a sword of the same material, reach further, and sweep like swords. Sword enchantments work on them, except **Fire Aspect**: the unique scythes bring their own effects. In your hand they show a long reaper blade.

- **Material scythes:** wood, stone, copper, gold, iron and diamond. Craft them from 2 of the material, a Spectral Binder and 2 sticks. The netherite scythe is a smithing upgrade from the diamond one, like other netherite gear.
- **Harvesting:** right-click a fully grown crop with any scythe to harvest it with **double the drops**. It is replanted automatically, and fully grown crops directly next to it (north, south, east and west) are harvested too. Each click costs one durability. Works on wheat, carrots, potatoes, beetroots and nether wart.

### Unique scythes

Each crystal family has its own unique scythe with netherite strength. It is crafted like the material scythes with 2 crystals of its family and repaired with them. A **fully charged hit** triggers a special effect:

| Scythe | Family | Charged-hit effect |
|---|---|---|
| Crimson Diabell | Crimson Sanguis | **Bleed:** the target loses health every second for 5 s, twice as much while it keeps moving. |
| Melting Sunray | Cruel Sun | **Meltdown:** melts the target's armor: −50 % armor for 6 s, dripping molten metal. |
| Blitz Slasher | Storm Fang | **Blitz Rush:** you move 50 % faster for 3 s (adds up with Stormfather's Heirloom). |
| Overgrown Reaper | Overgrown Prosperity | **Leech Seed:** drains the target's health for 5 s and heals you by the same amount. |
| Scythe of the Frozen Expanse | Ice Star | **Freeze:** slows the target to a quarter of its speed for 3 s. |
| The Shallow Waters | Cerulean Abyss | **Tidecaller:** for 8 s every hit heals you 1 health, or 2 while you're in water or rain. |
| Scorpion Tail | Venom Crown | **Venom Stack:** poisons the target for 6 s. Each further charged hit adds a stack (up to 5), and more stacks deal more damage. |

Each effect has its own particles and status icon, and the scythe's tooltip describes it.

## Planned

- The **Light** and **Dark** endgame families.

## License

MIT, see [LICENSE](LICENSE).
