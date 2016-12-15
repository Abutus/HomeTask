import java.time.*;

/**
 * @author abutus
 * @scine 1.12.2016.
 */
public class CarsNumberRunner {
    private static String fileName = "D://file.txt";

    public static void main(String[] args) {
        //Запись в файл
        FileWorker.writeOriginalFile(fileName);

        long start  = Clock.systemDefaultZone().millis();
        //Первоначальное разбиение на файлы
        FileWorker.primaryDividing(fileName);
        //Сортировка временных файлов
        FileWorker.fileMergeSort();
        long end  = Clock.systemDefaultZone().millis();

        System.out.println("The sorting lasted for " + (end - start)/1000 + "seconds");
    }
}
