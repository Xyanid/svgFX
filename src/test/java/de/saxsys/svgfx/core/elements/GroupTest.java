/*
 *
 * ******************************************************************************
 *  * Copyright 2015 - 2015 Xyanid
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

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
