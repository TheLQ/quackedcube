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
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import java.awt.Color;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
/**
 * Appender for everything thats not bot. All events from Bot are ignored
 *
 * @author Lord.Quackstar
 */
public class LoggingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	private PatternLayout normalGen = new PatternLayout();
	protected final JTextPane pane;
	protected final JScrollPane scroll;
	protected final SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss a");

	public LoggingAppender(JTextPane pane, JScrollPane scroll, LoggerContext context) {
		this.pane = pane;
		this.scroll = scroll;
		setName("LoggingAppender");
		setContext(context);
		normalGen.setContext(context);
		normalGen.setPattern("%d{MM/dd/yyy hh:mm:ss a}  %-5p %c - ");
		normalGen.start();
		start();
	}

	/**
	 * Used by Log4j to write something from the LoggingEvent. This simply points to
	 * WriteOutput which writes to the the GUI or to the console
	 * @param event
	 */
	@Override
	public void append(ILoggingEvent event) {
		//Write to console
		PrintStream output = (event.getLevel().isGreaterOrEqual(Level.WARN)) ? System.err : System.out;
		if (event.getThrowableProxy() == null)
			output.println(normalGen.doLayout(event) + event.getFormattedMessage());
		else
			output.println(event.getFormattedMessage() + "\n" + ExceptionUtils.getFullStackTrace(((ThrowableProxy) event.getThrowableProxy()).getThrowable()));


		//Write to GUI
		SwingUtilities.invokeLater(new WriteOutput(event));
	}

	/**0
	 * Utility for writing to output TextFields on GUI using standard format.
	 * <p>
	 * This should ONLY be executed in AWT Event Queue
	 * @author Lord.Quackstar
	 */
	public class WriteOutput implements Runnable {
		/**
		 * StyledDocument of pane
		 */
		StyledDocument doc;
		/**
		 * Date formatter, used to get same date format
		 */
		ILoggingEvent event;

		/**
		 * Simple constructor to init
		 * @param appendTo JTextPane to append to
		 */
		public WriteOutput(ILoggingEvent event) {
			this.doc = pane.getStyledDocument();
			this.event = event;

			//Only add styles if they don't already exist
			if (doc.getStyle("Class") == null) {
				doc.addStyle("Normal", null);
				StyleConstants.setForeground(doc.addStyle("Class", null), Color.blue);
				StyleConstants.setForeground(doc.addStyle("Error", null), Color.red);
				StyleConstants.setItalic(doc.addStyle("Thread", null), true);
				StyleConstants.setItalic(doc.addStyle("Level", null), true);
			}
		}

		/**
		 * Actually formats and add's the text to JTextPane in AWT Event Queue
		 */
		@Override
		public void run() {
			try {
				//get string version
				String message = event.getFormattedMessage().trim();

				//don't print empty strings
				if (StringUtils.isBlank(name.trim()))
					return;

				Style msgStyle = null;
				if (event.getLevel().isGreaterOrEqual(Level.WARN))
					msgStyle = doc.getStyle("Error");
				else
					msgStyle = doc.getStyle("Normal");

				doc.insertString(doc.getLength(), "\n", doc.getStyle("Normal"));
				int prevLength = doc.getLength();
				doc.insertString(doc.getLength(), "[" + dateFormatter.format(event.getTimeStamp()) + "] ", doc.getStyle("Normal")); //time
				//doc.insertString(doc.getLength(), "["+event.getThreadName()+"] ", doc.getStyle("Thread")); //thread name
				doc.insertString(doc.getLength(), event.getLevel().toString() + " ", doc.getStyle("Level")); //Logging level
				doc.insertString(doc.getLength(), event.getLoggerName() + " ", doc.getStyle("Class"));
				doc.insertString(doc.getLength(), formatMsg(event, message), msgStyle);

				//Only autoscroll if the scrollbar is at the bottom
				//JScrollBar scrollBar = scroll.getVerticalScrollBar();
				//if (scrollBar.getVisibleAmount() != scrollBar.getMaximum() && scrollBar.getValue() + scrollBar.getVisibleAmount() == scrollBar.getMaximum())
				pane.setCaretPosition(prevLength);
			} catch (Exception e) {
				e.printStackTrace(); //Don't use log.error because this is how stuff is outputed
			}
		}

		public String formatMsg(ILoggingEvent event, String message) {
			ThrowableProxy throwArr = (ThrowableProxy) event.getThrowableProxy();
			if (throwArr == null)
				return message;
			return message + "\n" + ExceptionUtils.getFullStackTrace(((ThrowableProxy) throwArr).getThrowable()).trim();
		}
	}
}
