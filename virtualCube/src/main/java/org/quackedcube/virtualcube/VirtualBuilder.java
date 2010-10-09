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

import ch.randelshofer.geom3d.JCanvas3D;
import ch.randelshofer.geom3d.Scene3D;
import ch.randelshofer.geom3d.Transform3DModel;
import ch.randelshofer.rubik.Cube3DCanvas;
import ch.randelshofer.rubik.Cube3DCanvasGeom3D;
import ch.randelshofer.rubik.DefaultCubeAttributes;
import ch.randelshofer.rubik.RubiksCube;
import ch.randelshofer.rubik.RubiksCubeGeom3D;
import ch.randelshofer.rubik.parser.DefaultNotation;
import ch.randelshofer.rubik.parser.Notation;
import idx3d.idx3d_JCanvas;
import idx3d.idx3d_Scene;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class VirtualBuilder extends JFrame {
	private Notation notation = new DefaultNotation();
	private Cube3DCanvas canvas;
	private RubiksCubeGeom3D cube3d;
	private RubiksCube cube;

	public VirtualBuilder() {
		super("CubeTwister Rubik's Cube Panel Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(createCube());
		setSize(400, 400);
		setVisible(true);
	}

	public JPanel createCube() {
		JPanel panel = new JPanel();
		// Initializes the components and adds them to the panel
		panel.setLayout(new java.awt.BorderLayout());

		// Creates a 3D model of the Rubik's Cube for the Idx3D rendering engine,
		// and turns animation on.
		cube3d = new RubiksCubeGeom3D();
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
		canvas = new Cube3DCanvasGeom3D();
		canvas.setCube3D(cube3d);

		// Disable all interaction with the 3D canvas
		canvas.setEnabled(false);

		// Add the canvas to the panel
		panel.add(canvas.getVisualComponent());

		//Rotate
		new Thread() {
			public void run() {
				try {
					//rotateidx3d();
					rotateGeom3D();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public void rotateGeom3D() throws InterruptedException {
				JCanvas3D jcanvas = (JCanvas3D)canvas.getVisualComponent();
				Scene3D scene = jcanvas.getScene();
				//canvas3D.setTransform(new Transform3D(0,Math.PI,Math.PI));
				//Transform3D transform = scene.getTransform();
				Transform3DModel transformModel = jcanvas.getTransformModel();
				while (true) {
					transformModel.rotate((float) 0.03, (float) 0.05, 0);
					Thread.sleep(50);
				}
			}

			public void rotateidx3d() throws InterruptedException {
				idx3d_Scene scene = ((idx3d_JCanvas) canvas.getVisualComponent()).getScene();
				while (true) {
					scene.rotate((float) 0.03, (float) 0.05, 0);
					Thread.sleep(50);
				}
			}
		}.start();

		return panel;
	}

	public static void main(String[] args) {
		new VirtualBuilder();
	}
}
