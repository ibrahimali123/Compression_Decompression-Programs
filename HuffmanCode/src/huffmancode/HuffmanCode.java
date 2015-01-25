/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancode;

import java.io.File;

/**
 *
 * @author Ibrahim Ali
 */
public class HuffmanCode {
 

    public static void main(String[] args){

        String textCase1 = "aafffcccdddddddaaccffeeeeeeaaaabbbaaaaaaaaaaaaaaaaaaabbddddddddeeecccbbbbbbaccccbba";
        String textCase2 = "aabcdaaaaa";
        String textCase3 = "ibrahim ali mohamed ebeido";
        
        GUI_Form obj = new GUI_Form();
        obj.setVisible(true);
        
        File ff = new File("compressed.txt");
        System.out.println(ff.length());
        
        File f = new File("FinalCompressedFile.txt");
        System.out.println(f.length());
 

    }

}
