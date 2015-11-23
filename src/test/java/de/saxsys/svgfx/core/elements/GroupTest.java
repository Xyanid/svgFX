package de.saxsys.svgfx.core.elements;

import de.saxsys.svgfx.core.SVGParser;
import javafx.scene.Group;
import javafx.scene.shape.SVGPath;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Created by Xyanid on 05.10.2015.
 */
public final class GroupTest {

    /**
     *
     */
    @Test public void parse() {

        SVGParser parser;

        parser = new SVGParser();

        Assert.assertNull(parser.getResult());

        URL url = getClass().getClassLoader().getResource("de/saxsys/svgfx/core/elements/group.svg");

        Assert.assertNotNull(url);

        try {

            parser.parse(url.getFile());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertNotNull(parser.getResult());

        Assert.assertEquals(parser.getResult().getChildren().size(), 2);

        Assert.assertThat(parser.getResult().getChildren().get(0), new IsInstanceOf(SVGPath.class));

        Assert.assertThat(parser.getResult().getChildren().get(1), new IsInstanceOf(Group.class));

        Assert.assertEquals(((Group) parser.getResult().getChildren().get(1)).getChildren().size(), 4);

        Assert.assertThat(((Group) parser.getResult().getChildren().get(1)).getChildren().get(0), new IsInstanceOf(SVGPath.class));

        Assert.assertThat(((Group) parser.getResult().getChildren().get(1)).getChildren().get(1), new IsInstanceOf(Group.class));

        Assert.assertThat(((Group) parser.getResult().getChildren().get(1)).getChildren().get(2), new IsInstanceOf(Group.class));

        Assert.assertThat(((Group) parser.getResult().getChildren().get(1)).getChildren().get(3), new IsInstanceOf(Group.class));
    }
}
