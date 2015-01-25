/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagerw;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Shorouk
 */
public class ImageRW 
{	
    public static int width=0;
    public static int height=0;
    public static int mean_square_error = 0;
    public static int[][] readImage(String filePath)
    {
        File file=new File(filePath);
        BufferedImage image=null;
        try
        {
            image=ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        width=image.getWidth();
        height=image.getHeight();
        
        int[][] pixels = new int[height][width];

        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                int rgb=image.getRGB(x, y);
                int alpha=(rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb >> 0) & 0xff;

                pixels[y][x]=r;
               // System.out.print(r + " ");
            }
            //System.out.println();
        }
        
        return pixels;
    }
    public static void writeImage(int[][] pixels,String outputFilePath)
    {
        File fileout=new File(outputFilePath);
        BufferedImage image2=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB );

        for(int x=0;x<width ;x++)
        {
            for(int y=0;y<height;y++)
            {
                image2.setRGB(x,y,(pixels[y][x]<<16)|(pixels[y][x]<<8)|(pixels[y][x]));
            }
        }
        try
        {
            ImageIO.write(image2, "jpg", fileout);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static int[][] UQuantization(int[][] pixels ,int numOfLevel) throws IOException
    {        
        int steps = 256 / numOfLevel;
        int [] arr = new int[numOfLevel];
        
        BufferedWriter fileWriter = new BufferedWriter(new  FileWriter("test.txt",false));
        fileWriter.write(String.valueOf(numOfLevel));
        fileWriter.newLine();
        fileWriter.write(String.valueOf(width));
        fileWriter.newLine();
        fileWriter.write(String.valueOf(height));
        fileWriter.newLine();
        //System.out.println(message);
        fileWriter.close();
        
        int d = steps /2;
        arr[0] = d;
        for (int i = 1; i < numOfLevel; i++) 
        {
            arr[i] = arr[i-1] + steps;
        }
         
         for(int x=0;x<width;x++)
         {
            for(int y=0;y<height;y++)
            {
                for (int i = 1 ; i <= numOfLevel ; i++) {
                    if((pixels[y][x] / steps) == i-1 ){
                        mean_square_error +=   (int) Math.pow((arr[i-1] - pixels[y][x]),2);
                    }
                    
                }   
                pixels[y][x] = pixels[y][x] / steps; 
         }            
        }
        
        for(int x=0;x<width;x++)   
         {
            for(int y=0;y<height;y++)
            {
                System.out.print( pixels[y][x]+ " ");
            }
            System.out.println();
         } 
        
        mean_square_error /= (width*height);
        
        System.out.println("mean_square_error = " + mean_square_error);
        return pixels;        
    }
    public static int[][] DQuantization(String photoName) throws IOException
    {                
        int numOfLevel;
        
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        ArrayList<String> list = new ArrayList<>(); 
        
        String line = "";
        while ((line = reader.readLine()) != null) {
             list.add(line);
        }
        reader.close();
        
        numOfLevel = Integer.parseInt(list.get(0));
        width = Integer.parseInt(list.get(1));
        height = Integer.parseInt(list.get(2));
        
        int steps = 256 / numOfLevel;
        
        int [] arr = new int[numOfLevel];
        
        int [][] pixels = readImage(photoName);
        
        int d = steps /2;
        arr[0] = d;
        for (int i = 1; i < numOfLevel; i++) 
        {
            arr[i] = arr[i-1] + steps; 
        }  
         for(int x=0;x<width;x++)   
         {
            for(int y=0;y<height;y++)
            { 
                for (int i = 0 ; i < numOfLevel ; i++) { 
                   if ( pixels[y][x] == i ) { 
                       pixels[y][x] = arr[i];
                       break;
                   } 
            }                    
         }   
         
        } 
        
         writeImage(pixels, "decomp.jpg");
         return pixels;
    }
    
    public static void main(String[] args) 
    {

//        int img[][]=readImage("lena.jpg");
//        
//        int numOfLevel;
//        Scanner in = new Scanner(System.in);
//        numOfLevel = in.nextInt(); 
//        int img2[][] = UQuantization(img, numOfLevel);
//        writeImage(img2, "copy.jpg");
        Gui form = new Gui();
        form.setVisible(true);
    }

}

