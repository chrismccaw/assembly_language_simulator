package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This dialog appears while the assembly language is getting assembler.
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class AssemblerDialog extends JDialog {

	JLabel notice;

	public AssemblerDialog(JFrame parent) {
		super(parent, "Assembling Program...", true);

		JPanel b = new JPanel();
		// b.add(Box.createGlue());

		notice = new JLabel();
		setLocation(new Point(parent.getX() + (parent.getWidth() / 2 - 50),
				parent.getY() + (parent.getHeight() / 2 - 50)));
		notice.setFont(new Font("Helvetica", Font.BOLD, 16));
		notice.setForeground(new Color(102, 0, 51));

		b.setLayout(new BoxLayout(b, BoxLayout.PAGE_AXIS));
		JLabel executing = new JLabel("Assembly program is executing...");
		executing.setFont(new Font("Helvetica", Font.BOLD, 13));
		JLabel currentEx = new JLabel("Currently executing: ");
		currentEx.setFont(new Font("Helvetica", Font.BOLD, 13));
		b.add(executing);
		b.add(currentEx);
		b.add(notice);

		getContentPane().add(b);
		b.setBackground(Color.WHITE);

		changeNotice();
		setSize(300, 150);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public void changeNotice() {

		Runnable r = new Runnable() {

			private boolean finished = false;
			ArrayList<String> noticeCollection = new ArrayList<String>();

			@Override
			public void run() {
				int count = 0;
				noticeCollection.add("Lexical Analysis");
				noticeCollection.add("Syntax Analysis");
				noticeCollection.add("Code Generation");
				while (!finished) {
					notice.setText(noticeCollection.get(count));
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (count >= noticeCollection.size() - 1)
						finished = true;
					count++;
				}

				setVisible(false);
				dispose();
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	public static void main(String[] args) {
		AssemblerDialog f = new AssemblerDialog(new JFrame());
		f.setVisible(true);
	}
}
