# Fountain-fabric

Fabric mod component of Fountain. This mod changes some parts of the Minecraft Java edition server to make it more compatible with Minecraft Earth via the Fountain bridge.

## Building

To build, run `./gradlew build` from the project root directory. This will take some time to run if you have never built a Fabric mod from source before, as Gradle will need to download dependencies and run the Fabric Loom.

The build output will be in `/build/libs/`.

## Usage

This mod is only compatible with Minecraft 1.20.4 running as a standalone server.

Download the Fabric server launcher from here: https://fabricmc.net/use/server/ (make sure to choose Minecraft version 1.20.4, tested with Fabric loader version 0.15.6 but newer versions should work).

Run the server once to download the Minecraft server JAR and create the `mods` directory, or create the `mods` directory yourself. Copy the `fountain-fabric-<version>.jar` file from the build output directory (see above) to the `mods` directory. You will also need to download and install the Fabric API, which you can get from here: https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?version=1.20.4 (tested with version 0.94.1 but newer versions should work). Put the Fabric API JAR in the `mods` directory as well.
