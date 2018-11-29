---
title: "Packages"
category: Users
order: 2
---

The packaging is done in phoenicis-dist. 

The built packages are located in the target folder:
- .zip
- .deb


### Flatpak
Required extensions:
- org.freedesktop.Platform.Compat.i386
- org.freedesktop.Platform.GL32.nvidia-410-73

```
cd phoenicis-dist/src/flatpak/
flatpak-builder build-dir org.phoenicis.javafx.json --force-clean --user --install
```
run with:
```
flatpak run org.phoenicis.javafx phoenicis-javafx
```
**Hint:** If you cannot compile after building the flatpak, remove `build-dir` and `.flatpak-builder`.