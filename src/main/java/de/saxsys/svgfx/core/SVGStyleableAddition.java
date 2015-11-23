package de.saxsys.svgfx.core;


import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.Group;
import javafx.scene.shape.SVGPath;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is intended to allow the usage of svg file for skins. It simply contains a {@link Group} which holds the loaded svg data if any.
 * rico.hentschel on 21.10.2015.
 */
class SVGStyleableAddition extends StyleableAdditionBase {

    //region Classes

    /**
     * Contains all styleable CssMetaData needed.
     */
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>();

            styleables.add(SVG_URL);
            styleables.add(WILL_CACHE_SVG);

            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    //endregion

    //region Static

    /**
     * CssMetaData for to make the Url styleable via Css.
     */
    private static final CssMetaData<Styleable, String> SVG_URL = new CssMetaData<Styleable, String>("-saxsys-svg-url", StyleConverter.getStringConverter(), null) {
        /**
         * determines if the property can be set using Css
         *
         * @param node, node which contains the property
         * @return true if he property can be set, otherwise false
         */
        @Override public boolean isSettable(final Styleable node) {
            return !getStyleableAddition(node, SVGStyleableAddition.class).svgUrl.isBound();
        }

        /**
         * returns the property which is styleable
         *
         * @param node, node which contains the property
         * @return the property which is styleable
         */
        @Override public StyleableProperty<String> getStyleableProperty(final Styleable node) {
            return (StyleableProperty<String>) getStyleableAddition(node, SVGStyleableAddition.class).svgUrl;
        }
    };

    /**
     * CssMetaData for to make the WillCacheSvg styleable via Css.
     */
    private static final CssMetaData<Styleable, Boolean> WILL_CACHE_SVG = new CssMetaData<Styleable, Boolean>("-saxsys-will-cache-svg", StyleConverter.getBooleanConverter(), true) {
        /**
         * determines if the property can be set using Css
         *
         * @param node, node which contains the property
         * @return true if he property can be set, otherwise false
         */
        @Override public boolean isSettable(final Styleable node) {
            return !getStyleableAddition(node, SVGStyleableAddition.class).willCacheSvg.isBound();
        }

        /**
         * returns the property which is styleable
         *
         * @param node, node which contains the property
         * @return the property which is styleable
         */
        @Override public StyleableProperty<Boolean> getStyleableProperty(final Styleable node) {
            return (StyleableProperty<Boolean>) getStyleableAddition(node, SVGStyleableAddition.class).willCacheSvgProperty();
        }
    };

    //endregion

    //region Fields

    /**
     * This {@link SVGParser} will be used to convert the svg files into javafx data.
     */
    private final SVGParser parser;
    /**
     * This group will contain the nodes that will make up the graphic of the button this skin is applied to.
     */
    private final Group svgGroup = new Group();
    /**
     * Contains the data if cached svg files.
     */
    private final Map<String, Group> svgData = new HashMap<>();
    /**
     * Determines the Url Property.
     */
    private final ObjectProperty<String> svgUrl = new SimpleStyleableObjectProperty<>(SVG_URL, this, "svgUrl", null);
    /**
     * Determines the WillCacheSvg Property.
     */
    private final ObjectProperty<Boolean> willCacheSvg = new SimpleStyleableObjectProperty<>(WILL_CACHE_SVG, this, "willCacheSvg", true);

    //endregion

    //region Constructor

    /**
     * Creates a new instance.
     */
    public SVGStyleableAddition() {

        svgUrlProperty().addListener(this::loadSVG);

        parser = new SVGParser();
    }

    //endregion

    //region Getter/Setter

    /**
     * Returns the {@link SVGStyleableAddition#svgGroup}.
     *
     * @return the {@link SVGStyleableAddition#svgGroup}
     */
    public final Group getSvgGroup() {
        return svgGroup;
    }

    /**
     * Gets the value of the Url.
     *
     * @return the value of the Url
     */
    public final String getSvgUrl() {
        return svgUrl.get();
    }

    /**
     * sets the value of the Url.
     *
     * @param value value to be used
     */
    public final void setSvgUrl(final String value) {
        svgUrl.set(value);
    }

    /**
     * Gets the value of the WillCacheSvg.
     *
     * @return the value of the WillCacheSvg
     */
    public final Boolean getWillCacheSvg() {
        return willCacheSvg.get();
    }

    /**
     * Sets the value of the WillCacheSvg.
     *
     * @param value value to be used
     */
    public final void setWillCacheSvg(final Boolean value) {
        willCacheSvg.set(value);
    }

    //endregion

    //region Public Labeled

    /**
     * @return The CssMetaData associated with this class, which may include the CssMetaData of its super core.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    //endregion

    //region SVG Handling

    /**
     * Will be called when the svg url changes and thus loading the new svg path.
     *
     * @param observable, the property which changed
     * @param oldValue,   the old value of the property
     * @param newValue,   the new value of the property
     */
    private void loadSVG(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

        if (newValue != null) {

            File file = getFile(newValue);

            if (file == null || !file.isFile() || !file.exists()) {
                throw new IllegalArgumentException(String.format("Given file %s does not exist or is not a file.", newValue));
            }

            svgGroup.getChildren().clear();

            Group data;

            try {
                data = getData(file);
            } catch (NoSuchMethodException | SAXParseException | IOException e) {
                e.printStackTrace();
                return;
            }

            svgGroup.getChildren().add(data);
        }
    }

    /**
     * Returns a {@link File} which represents the given path information. The filepath can either be a direct path,
     * a path to resource data or a path relative to the executable
     *
     * @param filepath, path to the file
     *
     * @return a new {@link File} corresponding to the given path information or null if the filepath can not be
     * resolved
     */
    private File getFile(final String filepath) {

        // try to get the file directly
        File file = new File(filepath);
        if (file.exists()) {
            return file;
        }

        // try to find the file in the resources
        URL url = getClass().getClassLoader().getResource(filepath);
        if (url != null) {
            file = new File(url.getFile());
            if (file.exists()) {
                return file;
            }
        }

        // try to load the file relative to our location
        file = new File(Paths.get(Paths.get(".").toAbsolutePath().toString(), filepath).toString());
        if (file.exists()) {
            return file;
        }

        return null;
    }

    /**
     * Provides the data based on the cached data and the cache mode.
     *
     * @throws IOException thrown when there is a problem loading the file
     */
    private Group getData(final File file) throws NoSuchMethodException, SAXParseException, IOException {

        Group result;

        if (!getWillCacheSvg()) {
            result = loadData(file);
        } else {
            result = svgData.get(file.getAbsolutePath());
            if (result == null) {
                result = loadData(file);
                svgData.put(file.getAbsolutePath(), result);
            }
        }

        return result;
    }

    /**
     * Loads the data of the given file using the {@link #parser}, after the parsing was done the {@link #parser} will be cleared.
     *
     * @param file, file to be used
     *
     * @return the {@link Group} of the result from the {@link SVGParser}
     */
    private Group loadData(final File file) throws NoSuchMethodException, SAXParseException, IOException {

        parser.parse(file);

        Group result = parser.getResult();

        parser.clear();

        return result;
    }

    //endregion

    //region Properties

    /**
     * Returns the Url to the svg image to be used.
     *
     * @return the Url Property
     */
    public final ObjectProperty<String> svgUrlProperty() {
        return svgUrl;
    }

    /**
     * Returns the WillCacheSvg.
     *
     * @return the WillCacheSvg Property
     */
    public final ObjectProperty<Boolean> willCacheSvgProperty() {
        return willCacheSvg;
    }

    //endregion

    //region Public

    /**
     * Adds the given path to the svg group.
     *
     * @param path path to add
     */
    public void addPath(final SVGPath path) {
        svgGroup.getChildren().add(path);
    }

    //endregion
}
