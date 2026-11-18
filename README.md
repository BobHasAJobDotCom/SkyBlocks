# Sky Blocks (Fabric, Minecraft 1.21.11)

Two unbreakable, barrier-style blocks with their own creative tab ("Sky Blocks", icon = Block of Sky).

| Block | What it does |
|---|---|
| **Block of Sky** | Solid, full-bright light-blue sky block. Unbreakable in survival, no drops. |
| **End Block** | Solid block that draws the End portal effect on all 6 faces. Does not teleport. Unbreakable in survival, no drops. |

Both break instantly in creative, like barriers. Both are in the **Sky Blocks** creative tab.

## Build

1. Install JDK 21 and Gradle 9.2+ (or open the folder in IntelliJ IDEA, which sets Gradle up for you).
2. Once, in this folder: `gradle wrapper --gradle-version 9.2.1` (creates `./gradlew`).
3. `./gradlew build` (first run downloads Minecraft + mappings).
4. The jar is in `build/libs/skyblocks-1.0.0.jar`. Put it in your `mods` folder with Fabric API.
5. Dev client: `./gradlew runClient`.

Versions are in `gradle.properties`. Check https://fabricmc.net/develop for the newest Fabric API / Loom for 1.21.11.

## If the End Block looks wrong

This project was written without being able to compile against 1.21.11 (no access to Fabric's Maven when it was generated), so two methods are deliberately written *without* `@Override`:

- `EndBlockEntity.shouldDrawSide(Direction)` - vanilla only draws the top face. Symptom if it doesn't hook in: only the top of the block shows the portal.
- `EndBlockEntityRenderer.getTopYOffset()` / `getBottomYOffset()` - vanilla draws the portal as a 0.75-high surface. Symptom if they don't hook in: the portal looks like a flat slab inside the block.

If you see either symptom, add `@Override` to those methods. The compiler will then tell you the exact name/signature in your 1.21.11 Yarn mappings (open `EndPortalBlockEntity` and `EndPortalBlockEntityRenderer` in your IDE and copy the names).

## Textures

`textures/block/sky_block.png` and `end_block.png` are generated 16x16 placeholders (light-blue sky, dark-teal speckled End look). Swap in your own art any time - same file names.

## Known limitation

The Block of Sky is a full-bright sky-blue block, not a true skybox "window" that renders the live sky. A real skybox effect needs a custom render pipeline and shader; that is a good next step.
