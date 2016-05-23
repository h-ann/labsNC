package lab1;

import org.reflections.Reflections;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.*;
import java.util.*;

/**
 * Class for representing the results of measuring in .xlsx files with Apache POI API
 * @author Anna Hulita
 * @version 1.0
 */
public class Adv {

    /**
     * Set excel tables with result measurements and prints the charts
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void test() throws InstantiationException, IllegalAccessException, IOException {

        setTabbles(200,4000,500);
        printCharts();

    }
    /**
     * Create charts with comparison of time spent on sorting arrays with represented methods and fiiller types.
     * Draw charts in .xlsx file.
     * @throws IOException
     */
    private void printCharts() throws IOException {

        File myFile = new File("res.xlsx");
        FileInputStream fis = new FileInputStream(myFile);
        XSSFWorkbook  workbook = new XSSFWorkbook(fis);

        for(int i=0; i<workbook.getNumberOfSheets();i++ ) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            System.out.println("\n"+sheet.getSheetName());

            Iterator<Row> rowIterator = sheet.iterator();
            int rows = 0;
            int cells = 0;
            while (rowIterator.hasNext()) {
                rows++;
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    cells++;
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        default:
                    }
                }
                System.out.println("");
            }

            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 10, 10, 30);
            Chart chart = drawing.createChart(anchor);


            ChartLegend legend = chart.getOrCreateLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            //ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
            ChartAxis bottomAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.BOTTOM);
            ChartAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            ScatterChartData lineChartData = chart.getChartDataFactory().createScatterChartData();
            //LineChartData lineChartData = chart.getChartDataFactory().createLineChartData();

            List<ChartDataSource<Number>> list = new ArrayList<>();
            int numOfColumns = cells/rows;
            int c=0;
            for(int j = 0; j < numOfColumns; j++ ) {
                list.add(DataSources.fromNumericCellRange(sheet, new CellRangeAddress(1, rows-1, c, c)));
                c++;
            }

            Row firstRow = sheet.getRow(0);
            Cell cell;
            for(int j = 1; j < numOfColumns; j++ ){
                cell = firstRow.getCell(j);
                lineChartData.addSerie(list.get(0), list.get(j)).setTitle(cell.getStringCellValue());
                //lineChartData.addSeries(list.get(0), list.get(j));
            }
            chart.plot(lineChartData, bottomAxis, leftAxis);
        }

        FileOutputStream fileOut = new FileOutputStream("plot.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        System.out.println("charts have been plotted successfully :)");
    }

    /**
     * Create .xlsx file with tables of data.  Data is the measurements of execution time of sorting arrays methods.
     * Sorting methods are called for arrays with different size.
     * @param startSize  size af the smallest array
     * @param endSize  size of the biggest array
     * @param iterator  step for increasing the size of array
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    private void setTabbles(int startSize, int endSize, int iterator) throws InstantiationException, IllegalAccessException, IOException {
        XSSFWorkbook  workbook = new XSSFWorkbook();
        int[] mass;
        int[] cloneMass;
        List<Sorter> allsorters = new ArrayList<>();
        List<Filler> allfillers = new ArrayList<>();
        setClassesAndFillers(allsorters,allfillers);

        for (Filler f : allfillers) {
            XSSFSheet sheet = workbook.createSheet(String.valueOf(f.getClass()));
            int  rownum = 0;
            Row row = sheet.createRow(rownum++);
            int c=0;
            Cell cell = row.createCell(c++);
            cell.setCellValue((String)"size");

            for(Sorter s : allsorters) {
                cell = row.createCell(c++);
                String str = String.valueOf(s.getClass());
                cell.setCellValue(str.substring(15,str.length()));
            }

            for (int n = startSize; n<=endSize; n=n+iterator) {
                mass = new int [n];
                f.fill(mass);
                row = sheet.createRow(rownum++);
                cell = row.createCell(0);
                cell.setCellValue((Integer)n);
                int cellnum = 1;

                for (Sorter s : allsorters) {
                    cloneMass = clone(mass);

                    long t0 = System.nanoTime();
                    s.sort(cloneMass);
                    System.out.println(cloneMass[cloneMass.length/2]);
                    long duration = System.nanoTime() - t0;

                    cell = row.createCell(cellnum++);
                    cell.setCellValue((Long)duration);
                   // System.out.println("time=" + duration + "n=" + n + " filler:" + f.getClass() + " sorter:" + s.getClass());
                }
            }
        }
            FileOutputStream out = new FileOutputStream("res.xlsx");
            workbook.write(out);
            out.close();
            System.out.println("tables have been written successfully :)");


    }

    /**
     * Define all sorter and filler methods in the package
     * @param allsorters List<Sorter> to store all sorter classes
     * @param allfillers List<Filler> to store all filler classes
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void setClassesAndFillers(List<Sorter> allsorters, List<Filler> allfillers)
            throws IllegalAccessException, InstantiationException{

        Reflections reflectionsS = new Reflections("lab1");
        Set<Class<?>> sortclasses = reflectionsS.getTypesAnnotatedWith(SortClass.class);

        Reflections reflectionsF = new Reflections("lab1");
        Set<Class<?>> fillclasses = reflectionsF.getTypesAnnotatedWith(FillClass.class);

        for (Class<?> s : sortclasses) {
            allsorters.add((Sorter) s.newInstance());
        }

        for (Class<?> f : fillclasses) {
            allfillers.add((Filler) f.newInstance());
        }
    }

    private int[] clone (int[] mass){
        int[] clonemass = new int[mass.length];
        for (int i=0; i<mass.length; i++){
            clonemass[i]=mass[i];
        }
        return  clonemass;
    }
}
