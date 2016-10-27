package com.rebaze.mystique;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static okio.Okio.buffer;

/**
 * Created by tonit on 24/10/16.
 */
public class LocalCopyingMaterializer implements Materializer
{
    private final File target;

    public LocalCopyingMaterializer( File target, boolean pruneBefore )
    {
        this.target = target;
        if (pruneBefore) {
            try
            {
                delete( target );
                Files.deleteIfExists(target.toPath());
            }
            catch ( IOException e )
            {
                throw new IllegalArgumentException( "Cannot delete " + target,e );
            }
        }
    }

    private void delete( File f ) throws IOException
    {
        if ( f.isDirectory() )
        {
            for ( File sub : f.listFiles() )
            {
                delete( sub );
            }
        }
        f.delete();
    }

    public LocalCopyingMaterializer( File target )
    {
        this(target,false);
    }

    @Override
    public void materialize( Universe universe ) throws IOException
    {
        for ( MutableItem path : universe.getItems() )
        {
            if ( path.getTargetPath() != null )
            {
                File in = path.getUpdatedSource();
                File out = createOutput( path.getTargetPath());
                if (!out.getParentFile().exists()) {
                    out.getParentFile().mkdirs();
                }

                try ( BufferedSource source = buffer( Okio.source( in )))
                {
                    try ( BufferedSink sink = buffer(Okio.sink( out ))) {
                        sink.writeAll( source );
                    }
                }
            }
        }
    }

    private File createOutput( String path )
    {
        File f = new File(target,path);
        if (f.exists()) {
            //throw new IllegalStateException( "File " + f.getAbsolutePath() + " already exists. You have a conflict in your rule space." );
        }
        return f;
    }
}
