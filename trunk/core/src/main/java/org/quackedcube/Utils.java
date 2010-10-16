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

package org.quackedcube;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class Utils {
	public static Field getField(Object inst, String name) throws NoSuchFieldException, SecurityException {
		Field field = inst.getClass().getDeclaredField(name);
		field.setAccessible(true);
		return field;
	}

	public static Object executeMethod(Object inst, String name) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method curMethod = inst.getClass().getDeclaredMethod(name);
		curMethod.setAccessible(true);
		return curMethod.invoke(inst);
	}
}
