/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancode;

/**
 *
 * @author Ibrahim Ali
 */
public class HuffmanNode{
    String data = "";
    int freq = 0;
    String code = "";

    public HuffmanNode(){
        data = "";
        freq = 0;
        code = "";
    }
    public HuffmanNode(String dataa , int freqq , String codee) {
        data = dataa;
        freq = freqq;
        code = codee;
    }

    @Override
    public String toString (){
        return data + " " + code + " " + freq ;
    }
    
}