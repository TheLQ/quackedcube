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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import org.quackedcube.virtualcube.VirtualBuilder;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class Gui extends JFrame {
	JLabel clock;
	VirtualBuilder virtualCube;
	Dimension frameSize = new Dimension(800, 800);

	public Gui() {
		super("QuackedCube Controller");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(frameSize);
		setVisible(true);
		add(generateContent());
		validate();
		repaint();
	}

	public JComponent generateContent() {
		//Inital creation
		JPanel clockPanel = new JPanel(new BorderLayout());
		JPanel virtualCubePanel = new JPanel(new BorderLayout());
		JPanel logPanel = new JPanel(new BorderLayout());
		JSplitPane verticalSplit = Utils.createSplitPane(JSplitPane.HORIZONTAL_SPLIT, virtualCubePanel, logPanel);
		JSplitPane horozontalSplit = Utils.createSplitPane(JSplitPane.VERTICAL_SPLIT, clockPanel, verticalSplit);
		verticalSplit.setDividerLocation(400);
		horozontalSplit.setDividerLocation(200);
		virtualCubePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cube Position"));
		logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log"));

		//Clock setup
		clock = new JLabel("00:00.00", JLabel.CENTER);
		//clock.setForeground(Color.yellow);
		clock.setPreferredSize(new Dimension((int) clock.getPreferredSize().getWidth(), 100));
		clock.setFont(new Font("Arial", Font.PLAIN, 180));
		clock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Timer"));
		clock.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		clock.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		clockPanel.add(clock, BorderLayout.CENTER);

		//Logging panel setup
		JTextPane logPane = new JTextPane();
		logPanel.add(logPane, BorderLayout.CENTER);
		logPane.setText("Hello there buddy");

		//Virtual Cube panel
		virtualCubePanel.add(virtualCube = new VirtualBuilder(), BorderLayout.CENTER);
		JPanel virtualCubeControl = new JPanel();
		virtualCubeControl.setLayout(new BoxLayout(virtualCubeControl, BoxLayout.LINE_AXIS));
		virtualCubeControl.add(new JButton("Rotate") {
			{
				final JButton self = this;
				setAlignmentX(Component.CENTER_ALIGNMENT);
				addActionListener(new ActionListener() {
					final String start = "Rotate";
					final String end = "Stop Rotating";

					@Override
					public void actionPerformed(ActionEvent e) {
						if (self.getText().equals(start)) {
							Gui.this.virtualCube.rotate();
							self.setText(end);
						} else if (self.getText().equals(end)) {
							Gui.this.virtualCube.stopRotating();
							self.setText(start);
						}
					}
				});
			}
		});
		/*virtualCubeControl.add(new JButton("Reset Position") {{
			addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Gui.this.virtualCube.resetPosition();
					}
				});
		}
		});*/

		virtualCubePanel.add(virtualCubeControl, BorderLayout.SOUTH);

		return horozontalSplit;
	}
}
