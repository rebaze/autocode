package com.rebaze.bespoke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonit on 18/10/16.
 */
public class LocalUniverse implements Universe
{
    private final File source;
    private final List<MutableItem> collected;

    public LocalUniverse( File source ) throws IOException
    {
        this.source = source;
        collected = new ArrayList<>();
        walk( source, collected );
    }

    private void walk( File f, List<MutableItem> collected ) throws IOException
    {
        if ( f.isDirectory() )
        {
            for ( File sub : f.listFiles() )
            {
                walk( sub, collected );
            }
        }
        else
        {
            collected.add( new MutableItem(this, relPath( source, f)));
        }
    }

    @Override public List<MutableItem> getItems()
    {
        return collected;
    }

    private String relPath( File base, File f )
    {
        String rel = f.getAbsolutePath().substring( base.getAbsolutePath().length() + 1);
        return rel;
    }

    @Override
    public File locate( String path) throws FileNotFoundException
    {
        File f = new File(source,path);
        if (!f.exists()) {
            throw new FileNotFoundException( "Path " + path  + " does not exist under root " + source.getAbsolutePath() + "(Tried: " + f.getAbsolutePath() + ")" );
        }
        return f;
    }
}
