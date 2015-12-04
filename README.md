# svgFX
This library allows for svg graphics to be directly converted into native javaFX object.

[![Build Status](https://api.travis-ci.org/Xyanid/svgFX.svg?branch=master)](https://travis-ci.org/Xyanid/svgFX)

# Quick Example

In your FXML use the SVGGroup like this, note that you will need to import the group 
```xml
<?import de.saxsys.svgfx.core.*?>
...
<SVGGroup fx:id="svgGroup" GridPane.halignment="CENTER" GridPane.rowIndex="1" GridPane.valignment="CENTER" />
...
```
In your CSS style there are different ways to configure the path to the svg file you want to load.

##### Direct link
This is a direct link to the svg file you want to load.
```xml
#svgGroup {
	-saxsys-svg-url: "D:\svg\maximize.svg";
}
```
##### Resource folder
This will look up the file in the path of the resource folder.
```xml
#svgGroup {
	-saxsys-svg-url: "svg/maximize.svg";
}
```

### Note that I plan to extract the SVGGroup from the lib and put it inside an own library in the coming versions.
