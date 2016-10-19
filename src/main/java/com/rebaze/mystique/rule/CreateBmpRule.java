package com.rebaze.mystique.rule;

import com.rebaze.mystique.MutableItem;
import com.rebaze.mystique.MutationRule;
import com.rebaze.mystique.Universe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by tonit on 18/10/16.
 */
public class CreateBmpRule implements MutationRule
{
    CreateBmpRule(Universe universe) {
        // add item to universe when created.
    }

    @Override public boolean apply( MutableItem item )
    {
        BufferedImage img = map( 320, 160 );
        savePNG( img, "/tmp/out.bmp" );

        return false;
    }

    private static BufferedImage map( int sizeX, int sizeY ){
        final BufferedImage res = new BufferedImage( sizeX, sizeY, BufferedImage.TYPE_INT_RGB );
        for (int x = 0; x < sizeX; x++){
            for (int y = 0; y < sizeY; y++){
                res.setRGB(x, y, Color.WHITE.getRGB() );
            }
        }
        return res;
    }

    private static void savePNG( final BufferedImage bi, final String path ){
        try {
            RenderedImage rendImage = bi;
            ImageIO.write(rendImage, "bmp", new File(path));
            //ImageIO.write(rendImage, "PNG", new File(path));
            //ImageIO.write(rendImage, "jpeg", new File(path));
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

}
