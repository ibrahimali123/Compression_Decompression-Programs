/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Ibrahim Ali
 */
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}


public class GUI_Form extends javax.swing.JFrame {

    /**
     * Creates new form GUI_Form
     */
    public GUI_Form() {
        initComponents();
    }
    public static ArrayList<HuffmanNode> MapSort(Map<String, Integer> dictionary) {
        ArrayList<HuffmanNode> list = new ArrayList<>();

        ValueComparator bvc = new ValueComparator(dictionary);
        TreeMap<String, Integer> sorted_map = new TreeMap<>(bvc);
        sorted_map.putAll(dictionary);

        for (Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
            HuffmanNode obj = new HuffmanNode();
            String key = entry.getKey();
            Integer value = entry.getValue();
            obj.data = key;
            obj.freq = value;
            obj.code = "";
            list.add(obj);
        }

        return list;
    }

    public static ArrayList<HuffmanNode> recursion(ArrayList<HuffmanNode> list) {
        if (list.size() == 2) {
            list.get(0).code = "0";
            list.get(1).code = "1";
            return list;
        }
        
        ArrayList<HuffmanNode> temp = new ArrayList<HuffmanNode>();
        
        for (int i = 0; i < list.size(); i++) {
            temp.add(new HuffmanNode(list.get(i).data,list.get(i).freq,list.get(i).code ));
        }
 
        //System.err.println(temp);
        
        Map<String, Integer> dictionary = new HashMap<>();
        HuffmanNode obj = new HuffmanNode();
        
        /***********check for data with length > 1 ********/
        int index = -1;
        for (int i = 0; i < temp.size() ; i++) {
            if(temp.get(i).data.length() >1){
                index = i;
                break;
            }
        }
        
        String newData = "";
        int newFreq = 0;
        if(index == -1){
        obj = temp.get(temp.size() - 1);
        temp.remove(temp.size() - 1);

        newData = temp.get(temp.size()-1).data + obj.data;
        newFreq = temp.get(temp.size()-1).freq + obj.freq;

        temp.remove(temp.size()-1);    
        }
        else{
        obj = temp.get(temp.size() - 1);
        temp.remove(temp.size() - 1);

        newData = temp.get(index).data + obj.data;
        newFreq = temp.get(index).freq + obj.freq;

        temp.remove(index);
        }

        obj.data = newData;
        obj.freq = newFreq;
        obj.code = "";

        temp.add(obj);

        for (int i = 0; i < temp.size(); i++) {
            String data = temp.get(i).data;
            int freq = temp.get(i).freq;
            dictionary.put(data, freq);
        }
         
         // System.out.println("list 2 " + list.toString());
        //System.out.println("list : " + list);
        temp = MapSort(dictionary);
        dictionary.clear();
        ArrayList<HuffmanNode> l = recursion(temp);
       
        boolean flag = false;
        for (int i = 0; i < l.size(); i++) {
            flag = false;
            for (int j = 0; j < list.size(); j++) {
                if (l.get(i).data.equals(list.get(j).data)) {
                    list.get(j).code = l.get(i).code;
                   break;
                } else if (l.get(i).data.contains(list.get(j).data)) {
                    if (flag) {
                        list.get(j).code = l.get(i).code + "0";
                    } else {
                        list.get(j).code = l.get(i).code + "1";
                        flag = true;
                    }
                }
            }
        }
        return list;

    }
    
    public static int toDecimal(String str){
        double j=0;
        for (int i = 0; i < str.length(); i++) {  
                 if(str.charAt(i)== '1'){
                     j=j+ Math.pow(2,str.length()-1-i);
                 } 
        }
         
        return (int) j;
    }
    
    public static void encodeTOFile(ArrayList<HuffmanNode> list , String text) throws IOException {
        String x="",t="",res="";
        BufferedWriter fileW = new BufferedWriter(new  FileWriter("codeTable.txt",false));
        Map<String, String> dict = new HashMap<>();
        for (int i = 0; i < list.size() ; i++) {
            dict.put(list.get(i).data, list.get(i).code);
            fileW.write(list.get(i).data);
            fileW.newLine();
            fileW.write(list.get(i).code);
            fileW.newLine();
        }
        fileW.close(); 
        /*******************************************************************/  
        for (int i = 0; i < text.length() ; i++) {
            t = dict.get(String.valueOf(text.charAt(i)));
            for (int j = t.length() ; j < 31 ; j++) {
                x += "0";
            }
            x = x + t;
            res+=x;
            //res+="|";
            x = "";
        }
        BufferedWriter fileWriter = new BufferedWriter(new  FileWriter("compressed.txt",false));
        fileWriter.write(res);
        fileWriter.close();
        
        /******************************************************************/
        /*
        String line = "" , result = ""; 
        for (int i = 0 ; i < res.length() ; i+=31) {
            line = res.substring(i,i+31);
            
            int num = toDecimal(line);
            //System.out.println("line : " + line);
            //System.out.println("line : " + line.length());
            //System.out.println(num);
            result += String.valueOf(num);
        }  
        
        BufferedWriter finalFile = new BufferedWriter(new  FileWriter("FinalCompressedFile.txt",false));
        finalFile.write(result);
        finalFile.close();
        */
        FileOutputStream fos = new FileOutputStream("FinalCompressedFile.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos); 
        String line = "" , result = ""; 
        oos.writeInt(res.length()/31);
        for (int i = 0 ; i < res.length() ; i+=31) {
            line = res.substring(i,i+31);
            
            int num = toDecimal(line);
            oos.writeInt(num);
        }  
        oos.close();
        fos.close();
    }
    public static String Decoding() throws FileNotFoundException, IOException
    {
        String text = "" , result = "";
        
        BufferedReader reader = new BufferedReader(new FileReader("codeTable.txt"));
        ArrayList<String> list = new ArrayList<>();
        Map<Integer,String> dict = new HashMap<>();
        
        String line = "";
        while ((line = reader.readLine()) != null) {
             list.add(line);
        }
        reader.close();
        
        for (int i = 1; i < list.size() ; i+=2) {
             dict.put(toDecimal(list.get(i)), list.get(i-1));
        }
        //System.err.println(dict);
        
        /*
        BufferedReader br = new BufferedReader(new FileReader("compressed.txt"));
        text = br.readLine();
        br.close();
        
        line = ""; 
        for (int i = 0 ; i < text.length() ; i+=31) {
            line = text.substring(i,i+31);
            
            int num = toDecimal(line);
            //System.out.println("line : " + line);
            //System.out.println("line : " + line.length());
            //System.out.println(num);
            result += dict.get(num);
            line = "";
        }  
        System.out.println(result);
        BufferedWriter fileW = new BufferedWriter(new  FileWriter("FinalDecompressedFile.txt",false));
        fileW.write(result);
        fileW.close();
        */
        FileInputStream fis = new FileInputStream("FinalCompressedFile.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        int n = ois.readInt();
        for (int i = 0; i < n; i++) {
            int num = ois.readInt(); 
            result += dict.get(num);
        }
        ois.close();
        fis.close(); 
         
        System.out.println(result);
        
        BufferedWriter fileW = new BufferedWriter(new  FileWriter("FinalDecompressedFile.txt",false));
        fileW.write(result);
        fileW.close();
        return result;
    }
    public static void Encoding(String text) throws IOException {
        
        Map<String, Integer> dictionary = new HashMap<>();
        ArrayList<HuffmanNode> list = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            if (dictionary.containsKey(String.valueOf(text.charAt(i)))) {
                dictionary.put(String.valueOf(text.charAt(i)), dictionary.get(String.valueOf(text.charAt(i))) + 1);
            } else {
                dictionary.put(String.valueOf(text.charAt(i)), 1);
            }
        }
        
        String encodedText = "";
        double encodedNumber = 0;
        if (dictionary.size() == 1) {
            for (int i = 0; i <text.length() ; i++) {
                encodedText += "0";
            }
            // encodedNumber = toDecimal(encodedText);   
        }
               
        else{
            list = MapSort(dictionary);
         
            ArrayList<HuffmanNode> k = recursion(list);
            System.out.println(k);
            encodeTOFile(k,text);
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Choose File To Compress");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Compress");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jButton3.setText("Choose File To Decompress");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 533, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(48, 48, 48)
                                    .addComponent(jButton2))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(38, 38, 38)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton3)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton2)))
                    .addContainerGap(26, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser=new  JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try { 
                BufferedReader br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(Level.SEVERE, null, ex);
            }
 
            Scanner scan = null;
            try {
                scan = new Scanner(f);
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(Level.SEVERE, null, ex);
            }
                scan.useDelimiter("\\Z");
                String content = scan.next();
                jTextArea1.setText(content);
            
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        String text = jTextArea1.getText();
        
        try {
            Encoding(text);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        String st="File Compressed Successfuly";
        JOptionPane.showMessageDialog(null,st);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser=new  JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            BufferedReader br = null;
            
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(Level.SEVERE, null, ex);
            }
             
 
                Scanner scan = null;
            try {
                scan = new Scanner(f);
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(Level.SEVERE, null, ex);
            }
                scan.useDelimiter("\\Z");
                String content = null;
            try {
                content = Decoding();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(Level.SEVERE, null, ex);
            }
                jTextArea2.setText(content);
            
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI_Form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
