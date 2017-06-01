package com.rebaze.bespoke;

import java.io.IOException;

/**
 * Created by tonit on 24/10/16.
 */
public interface Materializer
{
    void materialize(Universe universe) throws IOException;
}
