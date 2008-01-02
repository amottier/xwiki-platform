package org.xwiki.platform.patchservice.api;

import java.util.List;

public interface RWPatchSet extends PatchSet
{
    void setVersionRange(List versions);

    void addVersion(PatchId version);

    void setPatches(List patches);

    void addPatch(Patch patch);

    void clearPatchset();
}
