/*
 * GeneTools.java
 * © 2020 Jeremy Brown
 * Licensed under Apache 2.0
 * @author brownje96
 */
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class GeneTools
    extends JFrame {

    private static GeneTools instance;

    private static final HashMap<String, String> codonValues = new HashMap<String, String>(); static {
        codonValues.put("UUU", "phe");
        codonValues.put("UUC", "phe");
        codonValues.put("UUA", "leu");
        codonValues.put("UUG", "leu");
        codonValues.put("CUU", "leu");
        codonValues.put("CUC", "leu");
        codonValues.put("CUA", "leu");
        codonValues.put("CUG", "leu");
        codonValues.put("AUU", "ile");
        codonValues.put("AUC", "ile");
        codonValues.put("AUA", "ile");
        codonValues.put("AUG", "met");  // START
        codonValues.put("GUU", "val");
        codonValues.put("GUC", "val");
        codonValues.put("GUA", "val");
        codonValues.put("GUG", "val");
        codonValues.put("UCU", "ser");
        codonValues.put("UCC", "ser");
        codonValues.put("UCA", "ser");
        codonValues.put("UCG", "ser");
        codonValues.put("CCU", "pro");
        codonValues.put("CCC", "pro");
        codonValues.put("CCA", "pro");
        codonValues.put("CCG", "pro");
        codonValues.put("ACU", "thr");
        codonValues.put("ACC", "thr");
        codonValues.put("ACA", "thr");
        codonValues.put("ACG", "thr");
        codonValues.put("GCU", "ala");
        codonValues.put("GCC", "ala");
        codonValues.put("GCA", "ala");
        codonValues.put("GCG", "ala");
        codonValues.put("UAU", "try");
        codonValues.put("UAC", "try");
        codonValues.put("UAA", "stop");     // STOP
        codonValues.put("UAG", "stop");     // STOP
        codonValues.put("CAU", "his");
        codonValues.put("CAC", "his");
        codonValues.put("CAA", "gln");
        codonValues.put("CAG", "gln");
        codonValues.put("AAU", "asn");
        codonValues.put("AAC", "asn");
        codonValues.put("AAA", "lys");
        codonValues.put("AAG", "lys");
        codonValues.put("GAU", "asp");
        codonValues.put("GAC", "asp");
        codonValues.put("GAA", "glu");
        codonValues.put("GAG", "glu");
        codonValues.put("UGU", "cys");
        codonValues.put("UGC", "cys");
        codonValues.put("UGA", "stop");     // STOP
        codonValues.put("UGG", "trp");
        codonValues.put("CGU", "arg");
        codonValues.put("CGC", "arg");
        codonValues.put("CGA", "arg");
        codonValues.put("CGG", "arg");
        codonValues.put("AGU", "ser");
        codonValues.put("AGC", "ser");
        codonValues.put("AGA", "arg");
        codonValues.put("AGG", "arg");
        codonValues.put("GGU", "gly");
        codonValues.put("GGC", "gly");
        codonValues.put("GGA", "gly");
        codonValues.put("GGG", "gly");
    }

    JMenuBar mnuBar = new JMenuBar();
    JMenu fileMnu = new JMenu("File"), editMnu = new JMenu("Edit");
    JMenuItem exitItm = new JMenuItem("Exit");

    // dummy items
    JMenuItem rnaDummyItm = new JMenuItem("RNA"),  dnaDummyItm = new JMenuItem("DNA"), naDummyItem = new JMenuItem("Nucleic Acid Tools");
    JMenuItem cvtToAminos = new JMenuItem("Interpret as Codons and Convert to Amino Acids");
    JMenuItem cvtToMRNAitm = new JMenuItem("Convert to Messenger RNA");
    JMenuItem cvtToDNAitm = new JMenuItem("Convert to DNA");
    JMenuItem reverseItm = new JMenuItem("Reverse Prime Direction");
    JMenuItem complementDNAItm = new JMenuItem("Complement DNA");
    JTextArea input = new JTextArea(), output = new JTextArea();

    public GeneTools() {
        // set window constraints
        setTitle("Genetic Tools");
        setSize(640, 480);
        setLayout(new GridLayout(2, 1));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // prepare Menu Bar
        setJMenuBar(mnuBar);

        mnuBar.add(fileMnu);
        mnuBar.add(editMnu);

        fileMnu.add(exitItm);
        exitItm.addActionListener(ae -> dispose());

        cvtToMRNAitm.addActionListener(ae -> output.setText(naFormat(input.getText()).replace("T", "U")));
        cvtToDNAitm.addActionListener(ae -> output.setText(naFormat(input.getText()).replace("U", "T")));
        reverseItm.addActionListener(ae -> output.setText(naFormat(new StringBuilder(input.getText()).reverse().toString())));
        complementDNAItm.addActionListener(ae -> {
            char dna[] = naFormat(input.getText()).toCharArray();
            for(int i = 0; i < dna.length; i++) {
                char c = dna[i];
                if(c == 'A') dna[i] = 'T';
                if(c == 'T') dna[i] = 'A';
                if(c == 'G') dna[i] = 'C';
                if(c == 'C') dna[i] = 'G';
            }
            output.setText(new String(dna));
        });
        cvtToAminos.addActionListener(ae -> {
            String inputRNA = naFormat(input.getText());
            if(isDNA(inputRNA)) {
                JOptionPane.showMessageDialog(this, "You cannot transcribe DNA into Amino Acids. Convert to mRNA First.");
                return;
            }
            if((inputRNA.length() % 3) != 0) {
                JOptionPane.showMessageDialog(this, "Sanitized message must be perfectly divisible by three (codons are three nucleotides long)");
            } else {
                StringBuilder br = new StringBuilder();
                for(int i = 0; i < inputRNA.length(); i+=3) {
                    System.out.println("querying codon " + inputRNA.substring(i, i+3));
                    br.append(codonValues.get(inputRNA.substring(i, i+3)));
                    if(i < inputRNA.length()-3) br.append('-');
                }
                output.setText(br.toString());
            }
        });

        rnaDummyItm.setEnabled(false);
        dnaDummyItm.setEnabled(false);
        naDummyItem.setEnabled(false);

        editMnu.add(naDummyItem);
        editMnu.add(reverseItm);

        editMnu.add(rnaDummyItm);
        editMnu.add(cvtToMRNAitm);
        editMnu.add(cvtToAminos);

        editMnu.add(dnaDummyItm);
        editMnu.add(cvtToDNAitm);
        editMnu.add(complementDNAItm);

        // prepare components
        input.setBorder(BorderFactory.createTitledBorder("Input"));
        input.setLineWrap(true);
        output.setBorder(BorderFactory.createTitledBorder("Output"));
        output.setLineWrap(true);

        // add components
        add(new JScrollPane(input));
        add(new JScrollPane(output));
        validate();
    }

    public static boolean isDNA(String source) {
        return source.contains("T");
    }

    public static String naFormat(String input) {
        return input.toUpperCase().trim().replace(" ", "");
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                instance = new GeneTools();
                instance.setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
