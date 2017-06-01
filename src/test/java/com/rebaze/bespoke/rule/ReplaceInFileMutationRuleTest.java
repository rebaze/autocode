package com.rebaze.bespoke.rule;

import com.rebaze.bespoke.MutableItem;
import com.rebaze.bespoke.MutationContext;
import com.rebaze.bespoke.Universe;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.rebaze.bespoke.MutationBuilder.mutation;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by tonit on 24/10/16.
 */
public class ReplaceInFileMutationRuleTest
{
    @Test
    public void testReplace() throws IOException
    {
        Universe universe = mock(Universe.class);

        MutableItem item = new MutableItem( universe,"/tmp/foo/bar" );
        MutableItem item2 = new MutableItem( universe,"/tmp/foo/other/context-bar.xml" );
        MutableItem itemNoMatch = new MutableItem( universe,"/tmp/bee/other/context-bar.xml" );

        when( universe.getItems()).thenReturn( Arrays.asList( item,item2,itemNoMatch ) );
        MutationContext ctx = new MutationContext();

        ctx.addRule( mutation().match(".*").copy());
        ctx.addRule( mutation().match(".*/foo/.*").replace( "/cheese" ));

        ctx.applyTo( universe );
        assertEquals("/tmp/foo/cheese",item.getTargetPath());
        assertEquals("/tmp/foo/other/context-cheese.xml",item2.getTargetPath());
        assertEquals("/tmp/bee/other/context-bar.xml",itemNoMatch.getTargetPath());
    }
}
