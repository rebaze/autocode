package com.rebaze.bespoke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by tonit on 18/10/16.
 */
public interface Universe
{

    List<MutableItem> getItems();

    File locate(String path) throws FileNotFoundException;
}
