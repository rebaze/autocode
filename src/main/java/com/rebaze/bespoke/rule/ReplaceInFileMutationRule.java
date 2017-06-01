package com.rebaze.bespoke.rule;

import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationRule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.rebaze.bespoke.Utils.readAll;

/**
 * Created by tonit on 18/10/16.
 */
public class ReplaceInFileMutationRule implements MutationRule
{
    private final String replacement;
    private final String origin;

    public ReplaceInFileMutationRule( String origin, String replacement )
    {
        this.origin = origin;
        this.replacement = replacement;
    }

    @Override public boolean apply( MutableItem item )
    {
        // create a temp file:
        try
        {
            String content = readAll(item.getUpdatedSource());
            content = content.replaceAll( origin,replacement );
            String last = item.getUpdatedSource().getName();
            File f = File.createTempFile("myst_",last,new File("target"));
            f.deleteOnExit();

            Files.write(f.toPath(), content.getBytes());
            item.updateModification(f);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return false;
        }
        // add an intermediate
        return true;
    }
}
