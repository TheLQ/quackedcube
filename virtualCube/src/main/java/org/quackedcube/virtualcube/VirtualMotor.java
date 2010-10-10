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

package org.quackedcube.virtualcube;

import ch.randelshofer.rubik.AbstractCube;
import java.security.InvalidParameterException;
import org.quackedcube.Motor;
import org.quackedcube.MotorPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class VirtualMotor implements Motor {
	protected final MotorPosition position;
	protected final AbstractCube cube;
	protected final int axis;
	protected final int layer;
	protected final int direction;
	protected boolean gripped = false;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public VirtualMotor(MotorPosition position, AbstractCube cube) {
		if (position == null || cube == null)
			throw new InvalidParameterException("Null parameter! Position: " + position + " | Cube: " + cube);

		this.position = position;
		this.cube = cube;

		if (position == MotorPosition.TOP) {
			axis = 1;
			layer = 4;
			direction = 1;
		} else if (position == MotorPosition.BOTTOM) {
			axis = 1;
			layer = 1;
			direction = 1;
		} else if (position == MotorPosition.LEFT) {
			axis = 0;
			layer = 4;
			direction = -1;
		} else if (position == MotorPosition.RIGHT) {
			axis = 0;
			layer = 1;
			direction = -1;
		} else
			//Somethings wrong!
			throw new InvalidParameterException("Unkown motor position! " + position);

		log.info("Virtual motor sucessfully created for "+position);
	}

	@Override
	public void spinLeft() {
		if (!gripped)
			return;
		cube.transform(axis, layer, direction);
	}

	@Override
	public void spinRight() {
		if (!gripped)
			return;
		cube.transform(axis, layer, -direction);
	}

	@Override
	public void grip() {
		gripped = true;
	}

	@Override
	public void release() {
		gripped = false;
	}
}
