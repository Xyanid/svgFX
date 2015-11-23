package de.saxsys.svgfx.core;

import de.saxsys.svgfx.core.definitions.Constants;
import de.saxsys.svgfx.core.elements.ClipPath;
import de.saxsys.svgfx.core.elements.Defs;
import de.saxsys.svgfx.core.elements.Group;
import de.saxsys.svgfx.core.elements.Line;
import de.saxsys.svgfx.core.elements.LinearGradient;
import de.saxsys.svgfx.core.elements.Path;
import de.saxsys.svgfx.core.elements.Polyline;
import de.saxsys.svgfx.core.elements.RadialGradient;
import de.saxsys.svgfx.core.elements.Rectangle;
import de.saxsys.svgfx.core.elements.Stop;
import de.saxsys.svgfx.core.elements.Style;
import de.saxsys.svgfx.core.elements.Svg;
import de.saxsys.svgfx.core.elements.Use;
import de.saxsys.svgfx.xml.elements.ElementBase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.Attributes;

/**
 * Created by Xyanid on 05.10.2015.
 */
public final class SVGElementCreatorTest {

    /**
     *
     */
    @Test public void createElements() {

        Attributes attributes = Mockito.mock(Attributes.class);

        SVGDataProvider dataProvider = new SVGDataProvider();

        SVGElementCreator creator = null;

        try {
            creator = new SVGElementCreator();
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }

        Assert.assertEquals(Constants.SVG_ELEMENT_CLASSES.size(), creator.getKnownClasses().size());

        ElementBase element = creator.createElement(Svg.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Svg.class);

        element = creator.createElement(Style.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Style.class);

        element = creator.createElement(Stop.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Stop.class);

        element = creator.createElement(Rectangle.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Rectangle.class);

        element = creator.createElement(RadialGradient.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), RadialGradient.class);

        element = creator.createElement(Path.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Path.class);

        element = creator.createElement(Polyline.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Polyline.class);

        element = creator.createElement(LinearGradient.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), LinearGradient.class);

        element = creator.createElement(Line.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Line.class);

        element = creator.createElement(Defs.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Defs.class);

        element = creator.createElement(Group.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Group.class);

        element = creator.createElement(Use.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), Use.class);

        element = creator.createElement(ClipPath.class.getAnnotation(SVGElementMapping.class).value(), attributes, null, dataProvider);
        Assert.assertNotNull(element);
        Assert.assertEquals(element.getClass(), ClipPath.class);
    }
}
