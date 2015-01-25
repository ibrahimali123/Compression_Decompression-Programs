 
package llz77_compression;

 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Llz77_compression {

    /**
     * @param args the command line arguments
     */
   
    public static void finishCompression() {
        try {
            String line , message = "" , pointer , len , matcher;
            Scanner s = new Scanner(new File("compressed.txt"));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()){
              list.add(s.next());
            }
            s.close();
            
            /***************************************************/
            for (int i = 0; i < list.size() ; i++) 
            {
                line = list.get(i);
                pointer = "" + line.charAt(0);
                     pointer = Integer.toString(Integer.parseInt(pointer),2);
                     if(pointer.length() < 3){
                         for(int j= pointer.length();j<3;j++){
                             pointer += "0";
                         }
                     }
                len = "" + line.charAt(1);
                     len = Integer.toString(Integer.parseInt(len),2);
                     if(len.length() < 3){
                         for(int j= len.length();j<3;j++){
                             len += "0";
                         }
                     }
                matcher = "" + line.charAt(2);
                     int ascii = (int) matcher.charAt(0);
                     matcher = Integer.toString(ascii,2);
                message += pointer + len + matcher;
                //int decimal = Integer.parseInt(message, 2); 
                
            }
            BufferedWriter fileWriter = new BufferedWriter(new  FileWriter("comp.txt",false));
            fileWriter.write(message); 
            System.out.println(message);
            fileWriter.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProgramForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Llz77_compression.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        ProgramForm form = new ProgramForm();
        form.setVisible(true); 
        
        //finishCompression();
    }
}
