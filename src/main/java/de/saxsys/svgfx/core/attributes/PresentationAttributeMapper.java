package de.saxsys.svgfx.core.attributes;

import de.saxsys.svgfx.content.ContentTypeBase;
import de.saxsys.svgfx.core.SVGDataProvider;
import de.saxsys.svgfx.core.content.SVGContentTypeBase;
import de.saxsys.svgfx.core.content.SVGContentTypeDouble;
import de.saxsys.svgfx.core.content.SVGContentTypeFillRule;
import de.saxsys.svgfx.core.content.SVGContentTypeLength;
import de.saxsys.svgfx.core.content.SVGContentTypePaint;
import de.saxsys.svgfx.core.content.SVGContentTypeString;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeDashArray;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeLineCap;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeLineJoin;
import de.saxsys.svgfx.core.content.SVGContentTypeStrokeType;
import de.saxsys.svgfx.core.elements.SVGClipPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * This class determines which svg presentation attributes are mapped to the desired {@link SVGContentTypeBase}.
 *
 * @author Xyanid on 09.03.2016.
 */
public class PresentationAttributeMapper extends BaseAttributeMapper<SVGDataProvider> {

    // region Constants

    /**
     * Determines the color of a stroke, this is either a name or a hexadezimal value representing the color.
     */
    public static final PresentationAttributeMapper STROKE = new PresentationAttributeMapper("stroke", SVGContentTypePaint::new);
    /**
     * Determines the type of stroke used.
     */
    public static final PresentationAttributeMapper STROKE_TYPE = new PresentationAttributeMapper("stroke-type", SVGContentTypeStrokeType::new);
    /**
     * Determines the controls the pattern of dashes and gaps used to stroke paths.
     */
    public static final PresentationAttributeMapper STROKE_DASHARRAY = new PresentationAttributeMapper("stroke-dasharray", SVGContentTypeStrokeDashArray::new);
    /**
     * Determines the dash offset of a stroke.
     */
    public static final PresentationAttributeMapper STROKE_DASHOFFSET = new PresentationAttributeMapper("stroke-dashoffset", SVGContentTypeLength::new);
    /**
     * Determines the shape to be used at the end of open subpaths when they are stroked.
     */
    public static final PresentationAttributeMapper STROKE_LINECAP = new PresentationAttributeMapper("stroke-linecap", SVGContentTypeStrokeLineCap::new);
    /**
     * Determines the shape to be used at the corners of paths or basic shapes when they are stroked.
     */
    public static final PresentationAttributeMapper STROKE_LINEJOIN = new PresentationAttributeMapper("stroke-linejoin", SVGContentTypeStrokeLineJoin::new);
    /**
     * Determines a limit on the ratio of the miter length to the stroke-width. When the limit is exceeded, the join is converted from a miter to a bevel.
     */
    public static final PresentationAttributeMapper STROKE_MITERLIMIT = new PresentationAttributeMapper("stroke-miterlimit", SVGContentTypeDouble::new);
    /**
     * Determines the opacity of the outline on the current object.
     */
    public static final PresentationAttributeMapper STROKE_OPACITY = new PresentationAttributeMapper("stroke-opacity", SVGContentTypeDouble::new);
    /**
     * Determines the width of the outline on the current object.
     */
    public static final PresentationAttributeMapper STROKE_WIDTH = new PresentationAttributeMapper("stroke-width", SVGContentTypeLength::new);
    /**
     * Represents a clip path which will be applied to the given element.
     */
    public static final PresentationAttributeMapper CLIP_PATH = new PresentationAttributeMapper("clip-path", SVGContentTypeString::new);
    /**
     * Represents the clip rule which determines how an element inside a {@link SVGClipPath} will be used.
     * It works like the {@link PresentationAttributeMapper#FILL_RULE}.
     */
    public static final PresentationAttributeMapper CLIP_RULE = new PresentationAttributeMapper("clip-rule", SVGContentTypeString::new);
    /**
     * Represents the color of the interior of the given graphical element.
     */
    public static final PresentationAttributeMapper FILL = new PresentationAttributeMapper("fill", SVGContentTypePaint::new);
    /**
     * Represents the color of the interior of the given graphical element.
     */
    public static final PresentationAttributeMapper FILL_OPACITY = new PresentationAttributeMapper("fill-opacity", SVGContentTypeDouble::new);
    /**
     * Represents the algorithm which is to be used to determine what side of a path is inside the shape.
     */
    public static final PresentationAttributeMapper FILL_RULE = new PresentationAttributeMapper("fill-rule", SVGContentTypeFillRule::new);
    /**
     * Represents the color to use for a stop.
     */
    public static final PresentationAttributeMapper STOP_COLOR = new PresentationAttributeMapper("stop-color", SVGContentTypePaint::new);
    /**
     * Represents the transparency of a gradient, that is, the degree to which the background behind the element is overlaid.
     */
    public static final PresentationAttributeMapper STOP_OPACITY = new PresentationAttributeMapper("stop-opacity", SVGContentTypeDouble::new);
    /**
     * Represents a color which cna be used for other rule such as fill, stroke or stop-color.
     */
    public static final PresentationAttributeMapper COLOR = new PresentationAttributeMapper("color", SVGContentTypePaint::new);
    /**
     * Represents the transparency of an element, that is, the degree to which the background behind the element is overlaid.
     */
    public static final PresentationAttributeMapper OPACITY = new PresentationAttributeMapper("opacity", SVGContentTypeDouble::new);

    /**
     * Contains all the values that are available for this attribute class.
     */
    public static final ArrayList<PresentationAttributeMapper> VALUES = new ArrayList<>(Arrays.asList(STROKE,
                                                                                                      STROKE_TYPE,
                                                                                                      STROKE_DASHARRAY,
                                                                                                      STROKE_DASHOFFSET,
                                                                                                      STROKE_LINECAP,
                                                                                                      STROKE_LINEJOIN,
                                                                                                      STROKE_MITERLIMIT,
                                                                                                      STROKE_OPACITY,
                                                                                                      STROKE_WIDTH,
                                                                                                      CLIP_PATH,
                                                                                                      CLIP_RULE,
                                                                                                      FILL,
                                                                                                      FILL_OPACITY,
                                                                                                      FILL_RULE,
                                                                                                      STOP_COLOR,
                                                                                                      STOP_OPACITY,
                                                                                                      COLOR,
                                                                                                      OPACITY));

    // endregion

    //region Constructor

    /**
     * {@inheritDoc}
     */
    public PresentationAttributeMapper(final String name, final Function<SVGDataProvider, ? extends SVGContentTypeBase> contentTypeCreator) {
        super(name, contentTypeCreator);
    }

    //endregion
}