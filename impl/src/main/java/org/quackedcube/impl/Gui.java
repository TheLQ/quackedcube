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

import ch.qos.logback.classic.Level;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.quackedcube.virtualcube.VirtualBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
public class Gui extends JFrame {
	Clock clock;
	VirtualBuilder virtualCube;
	Dimension frameSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	public final JTextPane logPane;
	public final JScrollPane logScroll;
	public final LoggingAppender appender;
	public final Logger log = LoggerFactory.getLogger(getClass());

	public Gui() {
		super("QuackedCube Controller");
		frameSize.height -= 40;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(frameSize);
		setVisible(true);

		//Logging
		logPane = new JTextPane();
		logScroll = new JScrollPane(logPane);
		ch.qos.logback.classic.Logger rootLog = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("root");
		rootLog.getLoggerContext().reset();
		rootLog.setLevel(Level.ALL);
		rootLog.detachAndStopAllAppenders();
		rootLog.addAppender(appender = new LoggingAppender(logPane, logScroll, rootLog.getLoggerContext()));

		//Add and paint
		add(generateContent());
		validate();
		repaint();
	}

	public JComponent generateContent() {
		//Inital creation
		JPanel clockPanel = new JPanel(new BorderLayout());
		JPanel virtualCubePanel = new JPanel(new BorderLayout());
		JPanel logPanel = new JPanel(new BorderLayout());
		virtualCubePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cube Position"));
		logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log"));

		//Clock setup
		clock = new Clock();
		clock.setPreferredSize(new Dimension((int) clock.getPreferredSize().getWidth(), 100));
		clock.setFont(new Font("Arial", Font.PLAIN, 180));
		clock.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Timer"));
		clock.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		clockPanel.add(clock, BorderLayout.CENTER);

		//Logging panel setup
		logScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logScroll.setAlignmentX(Component.RIGHT_ALIGNMENT);
		logPane.setEditable(false);
		logPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		logPanel.add(logScroll, BorderLayout.CENTER);

		//Virtual Cube panel
		log.trace("Creating virtual cube");
		virtualCubePanel.add(virtualCube = new VirtualBuilder(), BorderLayout.CENTER);
		log.trace("Done creating virtual cube.");
		JPanel virtualCubeControl = new JPanel();
		virtualCubeControl.setLayout(new BoxLayout(virtualCubeControl, BoxLayout.Y_AXIS));
		virtualCubeControl.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		virtualCubeControl.add(new JButton("Rotate") {
			{
				final JButton self = this;
				setAlignmentX(JComponent.CENTER_ALIGNMENT);
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
		virtualCubePanel.add(virtualCubeControl,BorderLayout.SOUTH);
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

		log.trace("Creating content pane");
		JPanel contentPane = new JPanel(new MigLayout("fill", "fill", "fill"));
		contentPane.add(clock,"dock north"); //span 2, hmax 25%, wrap
		contentPane.add(virtualCubePanel,"growprio 20");
		contentPane.add(logPanel,"span 1 2");

		return contentPane;
	}

	public class Clock extends JLabel implements MouseListener {
		boolean on = false;
		boolean watchRan = false;
		Timer timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StopWatch watch = Clock.this.watch;
				watch.split();
				Clock.this.setText(Clock.this.format(watch.getSplitTime()));
				Clock.this.validate();
				Clock.this.repaint();
				watch.unsplit();
			}
		});
		StopWatch watch = new StopWatch();
		private final Logger log = LoggerFactory.getLogger(this.getClass());

		public Clock() {
			super("", JLabel.CENTER);
			setText(format(watch.getTime()));
			addMouseListener(this);
			timer.setCoalesce(true);
		}

		protected String format(long time) {
			return DurationFormatUtils.formatDuration(time, "mm:ss.SSS", true);
		}

		public void start() {
			on = true;
			timer.start();
			if (!watchRan)
				watch.start();
			else
				watch.resume();
			log.debug("Starting...");
		}

		public void stop() {
			on = false;
			watchRan = true;
			watch.suspend();
			timer.stop();
			log.debug("Stopping");
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (on)
				stop();
			else
				start();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
