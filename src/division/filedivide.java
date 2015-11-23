package division;
import java.io.*;
import java.nio.channels.*;

public class filedivide {
    public  int spFile(String fa)  {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
          File f= new File(fa);                 

        int sizeOfFiles = 102400 ;//100kb
        byte[] buffer = new byte[sizeOfFiles];

        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(f))) {//try-with-resources to ensure closing stream
            String name = f.getName();

            int tmp = 0;
            while ((tmp = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                File newFile = new File(f.getParent(), "chunk" + "."+ Integer.toString(partCounter++));
                System.out.println(newFile);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, tmp);//tmp is chunk size
                }
            }
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("File divided in chunks of 100kb each");
        return partCounter--;
    }

    //public static void main(String[] args) throws IOException {
    //    splitFile(new File("CNHW1.pdf"));
   // }
}
	


