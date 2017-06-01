package com.rebaze.bespoke;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonit on 18/10/16.
 */
public class MutableItem
{
    private final String sourcePath;
    private final Universe universe;
    private String targetPath;
    // holds references to all intermediate sources.
    private List<File> sourceStack = new ArrayList<>(  );

    public MutableItem( Universe universe, String sourcePath, String targetPath ) throws IOException
    {
        this.sourcePath = sourcePath;
        this.universe = universe;
        this.sourceStack.add(universe.locate(sourcePath));
        this.targetPath = targetPath;
    }

    public MutableItem( Universe universe, String sourcePath ) throws IOException
    {
        this(universe,sourcePath,null);
    }

    @Override public String toString()
    {
        return "MutableItem sourcePath=" + sourcePath + ", targetPath=" + targetPath;
    }

    public String getSourcePath()
    {
        return sourcePath;
    }

    public File getUpdatedSource()
    {
        return sourceStack.get( sourceStack.size() - 1 );
    }

    public String getTargetPath()
    {
        return targetPath;
    }

    public void setTargetPath( String targetPath )
    {
        this.targetPath = targetPath;
    }

    public void updateModification( File f )
    {
        sourceStack.add(f);
    }
}
