package com.rebaze.bespoke.rule;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationRule;

import java.io.*;

import static com.rebaze.bespoke.Utils.readAll;

/**
 * Created by tonit on 26/10/16.
 */
public class MustacheRule implements MutationRule
{

    private final String template;
    private final Object object;
    private MustacheFactory mf;

    public MustacheRule( String templatePath, Object object )
    {
        this.template = templatePath;
        this.object = object;
        mf = new DefaultMustacheFactory(  );
    }

    public MustacheRule( Object object )
    {
        this.template = null;
        this.object = object;
        mf = new DefaultMustacheFactory(  );
    }

    @Override public boolean apply( MutableItem item )
    {
        // create a temp file:
        try
        {

            Mustache compiled = mf.compile( new InputStreamReader( MustacheRule.class.getResourceAsStream( template ) ),"template" );
            String last = item.getUpdatedSource().getName();
            File f = File.createTempFile("myst_",last,new File("target"));
            try (PrintWriter pw = new PrintWriter(f))
            {
                compiled.execute( pw, object );
            }
            System.out.println(readAll(f));

            f.deleteOnExit();

            //Files.write(f.toPath(), content.getBytes());
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
