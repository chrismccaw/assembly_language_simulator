package gui;

import high_level_parser.AssemblyHolder;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cpu.Simulator_X86;

/**
 * This is controller class in the MVC paradigm. This class controls all action
 * listeners that is used. Data can be sent and received from the view
 * <code>SimulatorView</code>. This class has access to the model
 * <code>Simulator_X86</code> where it can get and set data
 * 
 * @author Chris McCaw
 * @version 1.0
 * @uml.dependency supplier="gui.AssemblerDialog"
 */
public class SimulatorController{

	private SimulatorView view;
	private HighLevelGUI compiler;
	Simulator_X86 cpu;
	private ConsoleWindow console;
	private AboutDialog about;
	private HelpDialog help;
	private boolean stopped = false;
	Runnable runThread;
	private Object lock = new Object();
	private TemplateDialog templateDialog;

	public SimulatorController(SimulatorView view, Simulator_X86 cpu) {
		this.view = view;
		this.cpu = cpu;
		about = new AboutDialog(view);
		help = new HelpDialog();
		compiler = view.getHighLevelPanel();
		templateDialog = new TemplateDialog();
		view.addStepListener(new stepListener());
		view.addRunListener(new runListener());
		view.addSliderListener(new sliderListener());
		view.addStopListener(new stopListener());
		view.addTemplateBtnListener(new templateListener());

		view.addClearListener(new clearListener());
		view.addCompilerListener(new compilerListener());
		view.addResetListener(new resetListener());
		view.addAboutWindowListener(new aboutWindowListener());
		view.addHelpWindowListener(new helpWindowListener());
		view.addExitListener(new exitWindowListener());

	}

	class SharedListSelectionHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			try {
				String assemblyCode = view.codeSegment.getSelectionRows()[view.codeSegment
				                                                          .getSelectedRow()];
				String getHighLevel = "";
				int getLineNumber = 0;
				ArrayList<AssemblyHolder> instructionsStore = compiler
				.getCompiler().compiledInstructions();
				for (int i = 0; i < instructionsStore.size(); i++) {
					if (instructionsStore.get(i).assembly.equals(assemblyCode
							.trim())) {
						getHighLevel = instructionsStore.get(i).highLevel;
						getLineNumber = instructionsStore.get(i).LineNum;
						break;
					}
				}
				compiler.textEditor.findAndHighlight(getHighLevel, getLineNumber);
			} catch (Exception n) {
				System.out.println("High Level Does't exist");
			}
		}
	}

	class stepListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int pc = cpu.getProgramCounter();
			System.out.println("PC " + pc);
			view.codeSegment.highlightRow(pc);
			cpu.executeStepMode();
			cpu.getMemory().getStack();
			if (cpu.getInstructionManager().programIsFinished()) {
				view.clearTextFields();
			}
		}
	}

	class runListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			runThread = new Runnable() {

				@Override
				public synchronized void run() {
					while (!cpu.getInstructionManager().programIsFinished()) {
						if (stopped) {
							try {
								synchronized (lock) {
									lock.wait();
								}
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
						view.codeSegment.highlightRow(cpu.getProgramCounter());
						cpu.executeInstruction();
						try {
							Thread.sleep(view.getSliderValue());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			Thread runnableThread = new Thread(runThread, "Run Thread");
			runnableThread.start();

		}
	}

	class sliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			view.setSliderValue();

		}
	}

	class resetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {					
			view.clearStackTable();
			view.clearTextFields();
			view.resetFieldValues();
			view.codeSegment.highlightRow(0);		
			cpu.resetCurrentProgram();			
		}
	}

	class stopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (stopped) {
				stopped = false;
				synchronized (lock) {
					lock.notify();
				}
				ImageIcon stopIcon = new ImageIcon(new Object().getClass()
						.getResource("/images/stop.png"));
				view.getStopToggleBtn().setIcon(stopIcon);
			} else {
				stopped = true;
				ImageIcon goIcon = new ImageIcon(new Object().getClass()
						.getResource("/images/go.png"));
				view.getStopToggleBtn().setIcon(goIcon);
			}

		}
	}

	class clearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.editorPane.setText("");
		}
	}

	class compilerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (view.getAssignmentFillIn().checkAnswers(
					new StringBuffer(view.editorPane.getText()))
					&& !view.editorPane.getText().trim().isEmpty()) {

				view.assemblyProgram();
				if (cpu.canExecute()) {
					view.codeSegment
					.addTableListener(new SharedListSelectionHandler());


					AssemblerDialog f = new AssemblerDialog(view);
					f.setVisible(true);
					view.getTabs().setSelectedIndex(1);
					view.stepButton().setEnabled(true);
					view.runButton().setEnabled(true);
					view.getStopToggleBtn().setEnabled(true);
				}

			} else if (view.editorPane.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(view, "Enter some instructions",
						"Empty Program", JOptionPane.ERROR_MESSAGE);

			} else {
				view.getTabs().setSelectedIndex(0);
				System.out
				.println("Some Instructions are wrong - check again!");
				Object[] options = { "Fix Problems", "Continue", };
				int n = JOptionPane
				.showOptionDialog(
						view,
						"Line(s) "
						+ view.getAssignmentFillIn()
						.WronglineNumbers(
								new StringBuffer(
										view.editorPane
										.getText()))
										+ " are incorrect",
										"Incorrect Assembly Program",
										JOptionPane.YES_OPTION, JOptionPane.NO_OPTION,
										null, options, options[1]);

				if (n == 1) {
					if (view.editorPane.getText().contains("INCOMPLETE!!")) {
						JOptionPane.showMessageDialog(view,
								"Attempt to put an instruction",
								"Not complete", JOptionPane.ERROR_MESSAGE);
					} else {
						view.assemblyProgram();
						view.codeSegment
						.addTableListener(new SharedListSelectionHandler());
					}
				}
			}
		}

	}

	class templateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			templateDialog.setVisible(true);
			templateDialog.setLocation(new Point(view.getX()
					+ (view.getWidth() / 2 - 100), view.getY()
					+ (view.getHeight() / 2 - 100)));
		}
	}

	class consoleWindowListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (console.isVisible())
				console.setVisible(false);
			else
				console.setVisible(true);
		}
	}

	class aboutWindowListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			about.setVisible(true);
		}
	}

	class helpWindowListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			help.setVisible(true);
			help.setLocation(new Point(view.getX()
					+ (view.getWidth() / 2 - 100), view.getY()
					+ (view.getHeight() / 2 - 100)));
		}
	}

	class exitWindowListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

}
