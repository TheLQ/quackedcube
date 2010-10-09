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

import ch.randelshofer.rubik.Cube3DCanvas;
import ch.randelshofer.rubik.Cube3DCanvasIdx3D;
import ch.randelshofer.rubik.Cube3DEvent;
import ch.randelshofer.rubik.Cube3DListener;
import ch.randelshofer.rubik.DefaultCubeAttributes;
import ch.randelshofer.rubik.RubiksCube;
import ch.randelshofer.rubik.RubiksCubeIdx3D;
import ch.randelshofer.rubik.parser.DefaultNotation;
import ch.randelshofer.rubik.parser.Notation;
import ch.randelshofer.rubik.parser.ScriptParser;
import ch.randelshofer.rubik.parser.SequenceNode;
import ch.randelshofer.util.RunnableWorker;
import idx3d.idx3d_Camera;
import idx3d.idx3d_JCanvas;
import idx3d.idx3d_Matrix;
import idx3d.idx3d_Scene;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class Demo extends JPanel {
	private Notation notation = new DefaultNotation();
	private Cube3DCanvas canvas;
	private RubiksCubeIdx3D cube3d;
	private RubiksCube cube;

	public Demo() throws InterruptedException {
		// Initializes the components and adds them to the panel
		setLayout(new java.awt.BorderLayout());

		// Creates a 3D model of the Rubik's Cube for the Idx3D rendering engine,
		// and turns animation on.
		cube3d = new RubiksCubeIdx3D();
		cube3d.setAnimated(true);


		// Create a mathematical model of a Rubik's Cube and plugs it into
		// the 3D model.
		// Note: this is not necessary, since the constructor of RubiksCubeIdx3D
		// does this already.
		cube = new RubiksCube();
		cube3d.setCube(cube);

		// Orient the cube so that the faces front, up and right are visible
		DefaultCubeAttributes attr = (DefaultCubeAttributes) cube3d.getAttributes();
		attr.setAlpha((float) (Math.PI / -8f));
		attr.setBeta((float) (Math.PI / 4f));
		//attr.setStickerFillColor(0, Color.GREEN);

		// Create a cube 3D canvas with the Idx3d rendering engine and add
		// the cube to it.
		canvas = new Cube3DCanvasIdx3D();
		canvas.setCube3D(cube3d);

		// Disable all interaction with the 3D canvas
		//canvas.setEnabled(false);

		// Add the canvas to the panel
		add(canvas.getVisualComponent());
		setEnabled(false);

		//Solve
		Thread.sleep(1000);
		solve();

		//Rotate
		new Thread() {
			public void run() {
				try {
					idx3d_JCanvas jcanvas = (idx3d_JCanvas) canvas.getVisualComponent();
					idx3d_Scene scene = jcanvas.getScene();

					idx3d_Camera camera = scene.camera((String) getField(jcanvas, "cameraName").get(jcanvas));
					idx3d_Matrix camMx = (idx3d_Matrix) executeMethod(camera, "getMatrix");
					camMx.inverse();
					while (true) {
						scene.rotate((float) 0.03, (float) 0.05, 0);
						Thread.sleep(50);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
	}

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

	public static void main(String[] args) {
		try {
			JFrame f = new JFrame("CubeTwister Rubik's Cube Panel Demo");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.add(new Demo());
			f.setSize(400, 400);
			f.setVisible(true);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void solve() {
		ScriptParser parser = new ScriptParser(notation);
		try {
			final SequenceNode script = parser.parse("R2 R L B R2");

			new Thread(new RunnableWorker() {
				@Override
				public Object construct() {
					script.applyTo(cube, false);
					return null;
				}
			}).start();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					Demo.this,
					"<html><font face=Dialog><b>Script Error</b><br><font size=-1>" + e.getMessage(),
					"Apply",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
