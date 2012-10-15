package net.sourceforge.metware.binche.gui;

/* * Copyright (c) 2005 Flanders Interuniversitary Institute for Biotechnology (VIB)
 * *
 * * Authors : Steven Maere
 * *
 * * Modified by Stephan Beisken, European Bioinformatics Institute
 * *
 * * This program is free software; you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation; either version 2 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * * The software and documentation provided hereunder is on an "as is" basis,
 * * and the Flanders Interuniversitary Institute for Biotechnology
 * * has no obligations to provide maintenance, support,
 * * updates, enhancements or modifications.  In no event shall the
 * * Flanders Interuniversitary Institute for Biotechnology
 * * be liable to any party for direct, indirect, special,
 * * incidental or consequential damages, including lost profits, arising
 * * out of the use of this software and its documentation, even if
 * * the Flanders Interuniversitary Institute for Biotechnology
 * * has been advised of the possibility of such damage. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program; if not, write to the Free Software
 * * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * *
 * * Authors: Steven Maere
 * * Date: Apr.20.2005
 * * Class that extends JPanel and implements ActionListener. Makes
 * * a panel with a drop-down box of ontology choices. Last option custom... opens FileChooser   
 **/

import BiNGO.methods.BingoAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;

/**
 * ***************************************************************
 * ChooseOntologyPanel.java:   Steven Maere (c) April 2005
 * -----------------------
 * <p/>
 * Class that extends JPanel and implements ActionListener. Makes
 * a panel with a drop-down box of ontology choices. Last option custom... opens FileChooser
 * ******************************************************************
 */
public class ChooseOntologyPanel extends JPanel implements ActionListener {

    private final String CUSTOM = BingoAlgorithm.CUSTOM;

    /**
     * JComboBox with the possible choices.
     */
    public JComboBox choiceBox;
    /**
     * the selected file.
     */
    private File openFile = null;
    /**
     * parent window
     */
    private SettingsPanel settingsPanel;
    /**
     * default = true, custom = false
     */
    private boolean def = true;
    private String specifiedOntology;
    //private File annotationFilePath;
    private String[] choiceArray;

    /**
     * Constructor with a string argument that becomes part of the label of
     * the button.
     *
     * @param settingsPanel : parent window
     */
    public ChooseOntologyPanel(SettingsPanel settingsPanel, String[] choiceArray, String choice_def) {

        super();
        this.settingsPanel = settingsPanel;
        this.choiceArray = choiceArray;

        setOpaque(false);
        makeJComponents();
        setLayout(new GridLayout(1, 0));
        add(choiceBox);

        //defaults
        HashSet<String> choiceSet = new HashSet<String>();
        for (String s : choiceArray) {
            choiceSet.add(s);
        }
        if (choiceSet.contains(choice_def)) {
            choiceBox.setSelectedItem(choice_def);
        } else {
            choiceBox.removeActionListener(this);
            choiceBox.setEditable(true);
            choiceBox.setSelectedItem(choice_def);
            choiceBox.setEditable(false);
            specifiedOntology = BingoAlgorithm.CUSTOM;
            def = false;
            choiceBox.addActionListener(this);
        }
    }

    /**
     * Paintcomponent, part where the drawing on the panel takes place.
     */
    @Override
	public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }

    /**
     * Method that creates the JComponents.
     */
    public void makeJComponents() {

        choiceBox = new JComboBox(choiceArray);
        choiceBox.setEditable(false);
        choiceBox.addActionListener(this);
    }

    /**
     * Method that returns the selected item.
     *
     * @return String selection.
     */
    public String getSelection() {

        return choiceBox.getSelectedItem().toString();
    }

    public String getSpecifiedOntology() {

        return specifiedOntology;
    }

    /**
     * Method that returns 1 if one of default choices was chosen, 0 if custom
     */
    public boolean getDefault() {

        return def;
    }

    /**
     * Method performed when button clicked.
     *
     * @param event event that triggers action, here clicking of the button.
     */
    @Override
	public void actionPerformed(ActionEvent event) {

        if (choiceBox.getSelectedItem().equals(CUSTOM)) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
            int returnVal = chooser.showOpenDialog(settingsPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                specifiedOntology = CUSTOM;
                openFile = chooser.getSelectedFile();
                choiceBox.setEditable(true);
                choiceBox.setSelectedItem(openFile.toString());
                choiceBox.setEditable(false);
                def = false;
            }
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                choiceBox.setSelectedItem("ChEBI");
                specifiedOntology = "ChEBI";
                def = true;
            }
        } else {
            specifiedOntology = (String) choiceBox.getSelectedItem();
            def = true;
        }
    }
}
