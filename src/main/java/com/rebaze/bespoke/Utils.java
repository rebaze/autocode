package com.rebaze.bespoke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by tonit on 26/10/16.
 */
public class Utils
{
    public static String readAll( File f )
    {
        String input = "";

        try ( BufferedReader file = new BufferedReader( new FileReader( f ) ) )
        {

            String line;

            while ( ( line = file.readLine() ) != null )
                input += line + '\n';

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return input;
    }
}
