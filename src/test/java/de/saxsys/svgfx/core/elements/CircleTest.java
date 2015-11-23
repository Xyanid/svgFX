package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGParser;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Created by Xyanid on 05.10.2015.
 */
public final class CircleTest {

    /**
     *
     */
    @Test public void parse() {

        SVGParser parser;

        parser = new SVGParser();

        Assert.assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/elements/circle.svg");

        Assert.assertNotNull(url);

        try {

            parser.parse(url.getFile());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertNotNull(parser.getResult());

        Assert.assertEquals(parser.getResult().getChildren().size(), 2);

        Assert.assertThat(parser.getResult().getChildren().get(0), new IsInstanceOf(javafx.scene.shape.Circle.class));
    }
}
