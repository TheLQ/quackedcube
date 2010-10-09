/**
 * Copyright (C) 2010 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of QuackedCube.
 *
 * QuackedCube is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QuackedCube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QuackedCube.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.quackedcube.impl;

import java.awt.Component;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class Utils {
	public static JSplitPane createSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
		JSplitPane split = new JSplitPane(newOrientation, newLeftComponent, newRightComponent);
		// remove the border from the split pane
		split.setBorder(null);

		// set the divider size for a more reasonable, less bulky look
		split.setDividerSize(3);

		// check the UI.  If we can't work with the UI any further, then
		// exit here.
		if (!(split.getUI() instanceof BasicSplitPaneUI))
			return split;

		//  grab the divider from the UI and remove the border from it
		BasicSplitPaneDivider divider = ((BasicSplitPaneUI) split.getUI()).getDivider();
		if (divider != null)
			divider.setBorder(null);

		return split;
	}
}
