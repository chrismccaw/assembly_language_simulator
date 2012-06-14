package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This Dialog class contains information about the creator and the project
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class AboutDialog extends JDialog {
	public AboutDialog(JFrame parent) {
		super(parent, "About Assembly Language Language", true);

		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.PAGE_AXIS));
		b.add(new ImagePanel("/images/8086.jpg"));
		b.add(new JLabel("Assembly Language Simulator"));
		b.add(new JLabel("Final Year Software Engineering Project"));
		b.add(new JLabel("Created by: Chris McCaw"));
		b.add(new JLabel("Supervisor: Conor McBride"));

		getContentPane().add(b);
		b.setBackground(Color.WHITE);
		JPanel p2 = new JPanel();
		p2.setBackground(Color.WHITE);
		JButton ok = new JButton("Exit");
		p2.add(ok);
		getContentPane().add(p2, "South");

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});

		setSize(350, 250);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		JDialog f = new AboutDialog(new JFrame());
		f.setVisible(true);
	}
}
