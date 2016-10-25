package com.rebaze.mystique.rule;

import com.rebaze.mystique.LocalUniverse;
import com.rebaze.mystique.MutableItem;
import com.rebaze.mystique.MutationContext;
import com.rebaze.mystique.Universe;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.rebaze.mystique.MutationBuilder.mutation;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by tonit on 24/10/16.
 */
public class RenameRuleTest
{
    @Test
    public void testRenameOnSingle() throws IOException
    {
        Universe universe = mock(Universe.class);

        MutableItem item = new MutableItem( universe,"/pre/foo/bar" );
        item.setTargetPath( "/target/foo/bar" );
        RenameTargetPathRule rule = new RenameTargetPathRule( "bar","cheese" );
        rule.apply( item );
        assertEquals("/target/foo/cheese",item.getTargetPath());

    }

    @Test
    public void testRenameOnRoaster() throws IOException
    {
        Universe universe = mock(Universe.class);

        MutableItem item = new MutableItem( universe,"/tmp/foo/bar" );
        MutableItem item2 = new MutableItem( universe,"/tmp/foo/other/context-bar.xml" );
        MutableItem itemNoMatch = new MutableItem( universe,"/tmp/bee/other/context-bar.xml" );

        when( universe.getItems()).thenReturn( Arrays.asList( item,item2,itemNoMatch ) );
        MutationContext ctx = new MutationContext();

        ctx.addRule( mutation().match(".*").copy());
        ctx.addRule( mutation().match(".*/foo/.*").rename( "bar","cheese" ) );

        ctx.applyTo( universe );
        assertEquals("/tmp/foo/cheese",item.getTargetPath());
        assertEquals("/tmp/foo/other/context-cheese.xml",item2.getTargetPath());
        assertEquals("/tmp/bee/other/context-bar.xml",itemNoMatch.getTargetPath());
    }
}
