import java.io.*;
import java.util.*;

/**
 * @author abutus
 * @scine 12.12.2016.
 */
class FileWorker {

    private static int N;
    private static final int M = 65536; // max items the memory buffer can hold
    private static int slices;
    private static final String tmpPath = "D://tmpFiles/";

    static void writeOriginalFile(String fileName) {
        File file = new File(fileName);
        try {
            //проверяем, что если файл не существует то создаем его
            if (!file.exists()) {
                try {
                    if(!file.createNewFile()){
                        System.out.println("File Can't Be Create!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                //Записываем текст в файл
                List<Character> characters = createCharacters();
                for (Character ch1 : characters) {
                    for (int i = 9; i >= 0; --i) {
                        for (int j = 0; j <= 9; ++j) {
                            for (int k = 9; k >= 0; --k) {
                                for (Character ch2 : characters) {
                                    for (Character ch3 : characters) {
                                        out.println(ch1.toString() + i + j + k + ch2 + ch3 + "52 - Vasiliy Pupkin");
                                        ++N;
                                    }
                                }
                            }
                        }
                    }
                }
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Character> createCharacters() {
        List<Character> list = new ArrayList<>();
        for (int i = 1072; i < 1104; ++i) {
            list.add((char) i);
        }
        list.add('ё');
        return list;
    }

    static void primaryDividing(String fileName) {
        File myPath = new File(tmpPath);
        if(!myPath.mkdirs()){
            System.out.println("Directory Can't Be Create!");
        }
        try {
            File file = new File(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            slices = (int) Math.ceil((double) N / M);
            String[] buf;

            int i;
            // Iterate through the elements in the file
            for (i = 0; i < slices; ++i) {
                if(i == slices - 1){
                    buf = new String[N - i * M];
                }else {
                    buf = new String[M];
                }
                String s;
                int l = 0;
                // Read M-element chunk at a time from the file
                while (l < M && (s = in.readLine()) != null) {
                    buf[l] = s;
                    ++l;
                }
                // Sort M elements
                arrSort(buf, 0, buf.length - 1);

                // Write the sorted numbers to temp file
                PrintWriter out = new PrintWriter(new FileWriter(tmpPath + i + ".txt"));
                for (int k = 0; k < l; ++k) {
                    out.println(buf[k]);
                }
                out.close();
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
        } catch (IOException e1) {
            System.out.println("Problems with temp files!");
        }
    }

    private static void arrSort(String[] arr, int first, int last) {
        //реализация merge sort
        if(first >= last - 1) return;
        int mid = first + (last - first)/2;
        arrSort(arr, first, mid);
        arrSort(arr, mid, last);

        String[] buffer = new String[last - first];
        for(int i = first; i < last; ++i){
            buffer[i - first] = arr[i];
        }
        int l = 0, r = mid - first;
        for (int i = first; i < last; ++i)
        {
            if (l == mid - first)
                arr[i] = buffer[r++];
            else if (r == last - first)
                arr[i] = buffer[l++];
            else if (buffer[l].compareTo(buffer[r]) < 0)
                arr[i] = buffer[l++];
            else
                arr[i] = buffer[r++];
        }
    }

    static void fileMergeSort(){
        while (slices > 1) {
            int count = 0;
            for (int i = 0; i < slices; i += 2) {
                File file1 = new File(tmpPath + i + ".txt");
                if (!file1.exists())
                    throw new NullPointerException("Файл для сортировки не найден!");
                File file2 = new File(tmpPath + (i + 1) + ".txt");
                File tmpFile = new File(tmpPath + "tmp.txt");
                try {
                    if (!file2.exists()) {
                        if(!file1.renameTo(new File(tmpPath + count + ".txt")))
                            throw new FileNotFoundException();
                        break;
                    }
                    BufferedReader in1 = new BufferedReader(new FileReader(file1.getAbsoluteFile()));
                    BufferedReader in2 = new BufferedReader(new FileReader(file2.getAbsoluteFile()));
                    PrintWriter out = new PrintWriter(new FileWriter(tmpFile.getAbsoluteFile()));
                    try {
                        String s1 = in1.readLine(), s2 = in2.readLine();
                        while ((s1 != null && s1.length() != 0) && (s2 != null && s2.length() != 0)) {
                            if (s1.compareTo(s2) > 0) {
                                out.println(s2);
                                s2 = in2.readLine();
                            } else {
                                out.println(s1);
                                s1 = in1.readLine();
                            }
                        }
                        while ((s1 != null && s1.length() != 0)) {
                            out.println(s1);
                            s1 = in1.readLine();
                        }
                        while ((s2 != null && s2.length() != 0)) {
                            out.println(s2);
                            s2 = in2.readLine();
                        }

                        out.close();
                        in1.close();
                        if(!file1.delete()){
                            System.out.println("File Can't Be Delete!");
                        }
                        in2.close();
                        if(!file2.delete()){
                            System.out.println("File Can't Be Delete!");
                        }
                        if(!(tmpFile.renameTo(new File(tmpPath + count + ".txt")))) {
                            System.out.println("File Can't Be Rename!");
                        }
                        count++;
                    } finally {
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File Not Found!");
                } catch (IOException e1) {
                    System.out.println("Problems with temp files!");
                }
            }
            slices = (int) Math.ceil((double) slices / 2);
        }
    }
}