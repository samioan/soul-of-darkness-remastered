# Provenance

Soul of Darkness and its original code, data, branding and artwork belong to
their respective rights holders, including Gameloft. This workspace is an
independent reverse-engineering and porting project, not an official release.
No rights to the original game or recovered source are granted by this setup.

The supplied Soul-of-Darkness_J2ME_EN_v137.jar is preserved at the root and
copied to roms/ for the reference workflow. JARs, extracted/decoded assets,
screenshots, analysis output and fetched tool binaries are ignored by git.
Raw decompilation and renamed reference Java follow the sibling projects'
tracked-source convention; recovered code retains its original ownership.

Generic extraction/decompilation infrastructure, the symbol renamer, and VS
discovery were adapted from the three user-provided sibling workspaces:
tes-travels-decomp, gow-betrayal-decomp, rac-j2me-decomp. No license for that
code is inferred or assigned here. Tool provenance and checksums are recorded
in tools/README.md and tools/bootstrap.py. Vineflower is Apache-2.0 software;
MIDP/CLDC/Nokia stub binaries retain their respective owners' terms.

The native build/package contains the calibration scaffold and example
settings only. Players' game data must remain separate from future builds.
