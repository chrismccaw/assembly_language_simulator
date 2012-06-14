package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

/**
 * A console window appears at the bottom of the main JFrame
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class ConsoleWindow extends JFrame implements WindowListener,
		ComponentListener {

	private JPanel consolePanel;
	private SimulatorView view;

	public ConsoleWindow(SimulatorView view) {
		super("Console");
		this.view = view;

		setSize(new Dimension(700, 200));
		setBackground(Color.BLACK);
		add(editorPanel());

		addWindowListener(this);
		addComponentListener(this);
		view.addWindowListener(this);
		view.addComponentListener(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setVisible(false);
		setResizable(false);

	}

	public JPanel editorPanel() {
		consolePanel = new JPanel();
		consolePanel.setBackground(Color.BLACK);
		consolePanel.setPreferredSize(new Dimension(700, 200));
		JTextPane textEditor = new JTextPane();
		textEditor.setBackground(Color.BLACK);
		JScrollPane scroll = new JScrollPane(textEditor);
		// scroll.getViewport().setBackground(Color.BLACK);
		scroll.setOpaque(false);
		scroll.setPreferredSize(new Dimension(700, 190));
		consolePanel.add(scroll);
		return consolePanel;
	}

	public void setFramePosition(int x, int y) {
		setLocation(x, y);
		// TODO Auto-generated method stub
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		setFramePosition(view.getX(), view.getY() + view.getHeight() + 15);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
