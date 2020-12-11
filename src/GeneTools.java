/*
 * GeneTools.java
 * © 2020 Jeremy Brown
 * Licensed under Apache 2.0
 *
 * version 0.2
 *
 * @author brownje96
 */
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class GeneTools
    extends JFrame {

    private static final HashMap<String, String> codons = new HashMap<>(); static {
        codons.put("UUU", "phe");
        codons.put("UUC", "phe");
        codons.put("UUA", "leu");
        codons.put("UUG", "leu");
        codons.put("CUU", "leu");
        codons.put("CUC", "leu");
        codons.put("CUA", "leu");
        codons.put("CUG", "leu");
        codons.put("AUU", "ile");
        codons.put("AUC", "ile");
        codons.put("AUA", "ile");
        codons.put("AUG", "met");  // START
        codons.put("GUU", "val");
        codons.put("GUC", "val");
        codons.put("GUA", "val");
        codons.put("GUG", "val");
        codons.put("UCU", "ser");
        codons.put("UCC", "ser");
        codons.put("UCA", "ser");
        codons.put("UCG", "ser");
        codons.put("CCU", "pro");
        codons.put("CCC", "pro");
        codons.put("CCA", "pro");
        codons.put("CCG", "pro");
        codons.put("ACU", "thr");
        codons.put("ACC", "thr");
        codons.put("ACA", "thr");
        codons.put("ACG", "thr");
        codons.put("GCU", "ala");
        codons.put("GCC", "ala");
        codons.put("GCA", "ala");
        codons.put("GCG", "ala");
        codons.put("UAU", "try");
        codons.put("UAC", "try");
        codons.put("UAA", "stop");     // STOP
        codons.put("UAG", "stop");     // STOP
        codons.put("CAU", "his");
        codons.put("CAC", "his");
        codons.put("CAA", "gln");
        codons.put("CAG", "gln");
        codons.put("AAU", "asn");
        codons.put("AAC", "asn");
        codons.put("AAA", "lys");
        codons.put("AAG", "lys");
        codons.put("GAU", "asp");
        codons.put("GAC", "asp");
        codons.put("GAA", "glu");
        codons.put("GAG", "glu");
        codons.put("UGU", "cys");
        codons.put("UGC", "cys");
        codons.put("UGA", "stop");     // STOP
        codons.put("UGG", "trp");
        codons.put("CGU", "arg");
        codons.put("CGC", "arg");
        codons.put("CGA", "arg");
        codons.put("CGG", "arg");
        codons.put("AGU", "ser");
        codons.put("AGC", "ser");
        codons.put("AGA", "arg");
        codons.put("AGG", "arg");
        codons.put("GGU", "gly");
        codons.put("GGC", "gly");
        codons.put("GGA", "gly");
        codons.put("GGG", "gly");
    }

    final JMenuBar mnuBar = new JMenuBar();
    final JMenu fileMnu = new JMenu("File");
    final JMenu editMnu = new JMenu("Edit");
    final JMenuItem exitItm = new JMenuItem("Exit");

    // dummy items
    final JMenuItem rnaDummyItm = new JMenuItem("RNA");
    final JMenuItem dnaDummyItm = new JMenuItem("DNA");
    final JMenuItem naDummyItem = new JMenuItem("Nucleic Acid Tools");
    final JMenuItem cvtToAminos = new JMenuItem("Interpret as Codons and Convert to Amino Acids");
    final JMenuItem cvtToMRNAitm = new JMenuItem("Convert to Messenger RNA");
    final JMenuItem cvtToDNAitm = new JMenuItem("Convert to DNA");
    final JMenuItem reverseItm = new JMenuItem("Reverse Prime Direction");
    final JMenuItem complementDNAItm = new JMenuItem("Complement DNA");
    final JMenuItem formatItm = new JMenuItem("Format input to plain sequence format");

    final JTextArea input = new JTextArea();
    final JTextArea output = new JTextArea();

    public GeneTools() {
        // set window constraints
        setTitle("Genetic Tools by brownje96");
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
        complementDNAItm.addActionListener(ae -> output.setText(complement(input.getText())));
        formatItm.addActionListener(ae -> input.setText(naFormat(input.getText())));
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
                    br.append(codons.get(inputRNA.substring(i, i+3)));
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
        editMnu.add(formatItm);

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

    /**
     * Returns a complementary strand of DNA
     *
     * @param originalDNA string containing the shorthand bases of the original strand
     * @return complementary string containing the shorthand bases of the daughter strand
     */
    public static String complement(String originalDNA) {
        char dna[] = naFormat(originalDNA).toCharArray();
        for(int i = 0; i < dna.length; i++) {
            char c = dna[i];
            if(c == 'A') dna[i] = 'T';
            if(c == 'T') dna[i] = 'A';
            if(c == 'G') dna[i] = 'C';
            if(c == 'C') dna[i] = 'G';
        }
        return new String(dna);
    }

    public static boolean isDNA(String source) {
        return source.contains("T");
    }

    /**
     * Formats a string of nucleotides in "Plain sequence format"
     *
     * @param input a string of shortened nucleotides
     * @return a sanitized string of nucleotides.
     */
    public static String naFormat(String input) {
        return input.toUpperCase().trim().replace(" ", "");
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                JOptionPane.showMessageDialog(null, "THIS MACHINE HAS NO BRAIN. \nUSE YOUR OWN.\n\nThis tool is not all knowing.");
                new GeneTools().setVisible(true);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
