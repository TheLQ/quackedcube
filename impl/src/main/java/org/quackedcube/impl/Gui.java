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
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import org.quackedcube.virtualcube.VirtualBuilder;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class Gui extends JFrame {
	JLabel clock;
	Dimension frameSize = new Dimension(800,800);

	public Gui() {
		super("QuackedCube Robot Controller by Leon Blakey");
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
		JSplitPane verticalSplit = Utils.createSplitPane(JSplitPane.HORIZONTAL_SPLIT, virtualCubePanel,logPanel);
		JSplitPane horozontalSplit = Utils.createSplitPane(JSplitPane.VERTICAL_SPLIT, clockPanel, verticalSplit);

		//Setup

		verticalSplit.setDividerLocation(400);
		horozontalSplit.setDividerLocation(200);
		virtualCubePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cube Position"));
		logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log"));

		//Add components
		clock = new JLabel("00:00.00", JLabel.CENTER);
		//clock.setForeground(Color.yellow);
		clock.setPreferredSize(new Dimension((int)clock.getPreferredSize().getWidth(), 100));
		clock.setFont(new Font("Dialog", Font.PLAIN, 120));
		clock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Timer"));
		clockPanel.add(clock,BorderLayout.CENTER);
		virtualCubePanel.add(new VirtualBuilder(), BorderLayout.CENTER);
		JTextPane logPane = new JTextPane();
		logPanel.add(logPane,BorderLayout.CENTER);
		logPane.setText("Hello there buddy");
		
		return horozontalSplit;
	}
}
