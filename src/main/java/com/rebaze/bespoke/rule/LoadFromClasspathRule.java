package com.rebaze.bespoke.rule;

import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationRule;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;

import static okio.Okio.buffer;
import static okio.Okio.source;

/**
 *
 */
public class LoadFromClasspathRule implements MutationRule
{
    private final String path;

    public LoadFromClasspathRule( String path )
    {
        this.path = path;
    }

    @Override public boolean apply( MutableItem item )
    {
        // dump as file, then attach to source:
        try
        {
            File out = File.createTempFile( "myst_created_",".data", new File("target") );
            out.deleteOnExit();
            try (BufferedSource in =  buffer(source(LoadFromClasspathRule.class.getResourceAsStream( path )))){
                try ( BufferedSink sink = buffer( Okio.sink( out ))) {
                    sink.writeAll( in );
                }
            }
            item.updateModification( out );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot load from classpath: " + path );
        }
        return true;
    }
}
