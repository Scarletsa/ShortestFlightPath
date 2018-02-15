package application;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.MutableComboBoxModel;


/**
 * public class GuiManager extends JFrame implements ActionListener
 * 
 * This class will do all of the heavy
 * lifting needed for the program to run.
 * 
 * @author Michael Albrecht
 * @author Sarah Carr
 * @author Shelby Medlock
 * 
 * Filename: GuiManager.java
 * Course: CSCI 2082
 * College: Century
 * 
 */
public class GuiManager extends JFrame implements ActionListener {
	private JPanel main, north, bottom, top;
	private static JPanel dest;
	private JButton calculate;
	private JLabel source, destination, blank1, blank2;
	private JComboBox<String> srcCombo, destCombo;
	private static JTextArea textArea;

	// Constructor for the GUI
	public GuiManager(Map<String, Vertex> ports) {
		super("Flight Time Calculator");
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeComponents();
		Iterator it = ports.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (((Vertex) pair.getValue()).getAdjacencies() != null) {
				srcCombo.addItem(((Vertex) pair.getValue()).getIataCode());
			}
		}

		calculate.addActionListener(this);
		srcCombo.addActionListener(this);
		destCombo.addActionListener(this);
	}
	
	// Method to set up the components for the GUI.
	public void initializeComponents() {
		// Initialize JPanels
		setJPanels();

		// Initialize JButtons
		calculate = new JButton("Calculate Flight Time");

		// Initialize JLabels
		setJLabels();

		// Initialize JComboBox
		setJComboBox();

		// Initialize JTextArea
		textArea = new JTextArea();

		// Initialize Top Panel
		setTopPanel();

		// Initialize North Panel
		setNorthPanel();
		
		// Initialize Bottom Panel
		setBottomPanel();

		textArea.setEditable(false);

		setMain();

		add(main);
	}
	
	// Helper methods for generating the GUI.
	private void setJPanels() {
		main = new JPanel(new GridLayout(2, 1));
		top = new JPanel(new GridLayout(2, 2));
		north = new JPanel(new GridLayout(1, 3));
		bottom = new JPanel(new BorderLayout());
		dest = new JPanel(new GridLayout(1,0));
	}
	
	private void setJLabels() {
		source = new JLabel("Source:");
		destination = new JLabel("Destination:");
		blank1 = new JLabel(" ");
		blank2 = new JLabel(" ");
	}
	
	private void setJComboBox() {
		srcCombo = new JComboBox();
		destCombo = new JComboBox();
		destCombo.setEnabled(false);
		destCombo.addItem("");
		dest.add(destCombo);
	}
	
	private void setTopPanel() {
		top.add(source);
		top.add(srcCombo);
		top.add(destination);
		top.add(dest);
	}
	
	private void setNorthPanel() {
		north.add(blank1);
		north.add(calculate);
		north.add(blank2);
	}
	
	private void setBottomPanel() {
		bottom.add(north, BorderLayout.NORTH);
		bottom.add(textArea, BorderLayout.CENTER);
	}
	
	private void setMain() {
		main.add(top);
		main.add(bottom);
	}
	
	// Method to set the text in the GUI.
	public static void setTextArea(String text) {
		textArea.append(textArea.getText() + text);
		System.out.println("Inside setTextArea" + text);
	}

	// Method to add destinations to the destinations combo box.
	public static void addDestinations(Vertex v, MutableComboBoxModel m) {
		String paths = "";
		if (v.getAdjacencies() == null) {

		} else {
			for (Edge e : v.getAdjacencies()) {
				if (!(((DefaultComboBoxModel) m).getIndexOf(e.getTarget().getIataCode()) == -1)) {	
					m.addElement(e.getTarget().getIataCode());
					addDestinations(e.getTarget(), m);
				}
			}	
		}
	}
	
	// Method for reseting the destination combo box.
	public void resetDestCombo(String Source, MutableComboBoxModel m) {
		destCombo.setEnabled(false);
		dest.remove(destCombo);
		destCombo.setEnabled(true);
		destCombo = new JComboBox(m);
	    dest.add(destCombo);
	    dest.validate();
	}

	// Method for retrieving the name in the drop down menu of GUI
	public String getNameInSourceCombo() {
		srcCombo.getSelectedItem();
		return (String) srcCombo.getSelectedItem();
	}

	// Method for retrieving the name in the drop down menu of GUI
	public String getNameInDestinationCombo() {
		destCombo.getSelectedItem();
		return (String) destCombo.getSelectedItem();
	}

	// Action Performed methods for GUI
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(calculate)) {
			String src = getNameInSourceCombo();
			String dest = getNameInDestinationCombo();
			setTextArea(src + " " + dest);
			String output = Util.calculateFlightTime(src, dest);
			setTextArea(output);
		} else if (e.getSource().equals(srcCombo)) {
			MutableComboBoxModel model = new DefaultComboBoxModel();
			resetDestCombo(getNameInSourceCombo(), model);
			addDestinations(Util.getVertex(getNameInSourceCombo()), model);
			getNameInSourceCombo();
		} else if (e.getSource().equals(destCombo)) {
			getNameInDestinationCombo();
		}
	}
}
