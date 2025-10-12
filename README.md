# No Flame & Smoke (Fabric)

**Version:** 1.0.0  
**Minecraft:** 1.21.x (client-side)  
**License:** MIT

Hide flame/smoke particles for torches, candles and furnaces — per-block and per-effect (Flame / Smoke), with convenient global toggles.

---

## Features

- **Per-block switches:** Torch, Wall Torch, Redstone Torch, Wall Redstone Torch, Soul Torch, Wall Soul Torch, **Candles**, **Furnace**, **Smoker**, **Blast Furnace**.
- **Per-effect control:** separate **Flame** and **Smoke** toggles for each block.
- **Global toggles:** “All Flames” and “All Smoke” + **Apply to all** and **Restore vanilla**.
- **Mod Menu integration:** open the config screen from Mod Menu (optional).
- **Pure client-side:** safe to join servers; no server install required.
- **Resource-pack friendly UI:** uses vanilla GUI sprites (header separator & scroller) so your RP skins the screen automatically.

> UI semantics: **ON = particles visible**, **OFF = particles hidden**.

---

## Installation

1. Install **Fabric Loader** for Minecraft 1.21.x.
2. Drop this mod JAR into `.minecraft/mods/`.
3. (Optional) Install **Mod Menu** to access the config screen from the Mods list.

No hard dependency on Fabric API.

---

## Configuration

- **In-game:** via Mod Menu → *No Flame & Smoke*.
- **File:** `config/noflamesmoke.json` (created after first run).

JSON contains global flags:
```json
{ "disableAllFlames": false, "disableAllSmoke": false, ... }