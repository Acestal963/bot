package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.plaf.basic.BasicButtonUI;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;

public class Pedido {

    String producto;
    String cant;
    String precio;
    String guiso;
    int tiempo;
    int total;
    String ID;

    public String pdf(String cafeteria) throws IOException, DocumentException {
        String filePath = null;
        String ffff=null;
        String FILE_NAME=null;
        ID=ID();
        if (cafeteria.equals("Cafeteria_chica")) {
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PEDIDO.csv";
            ffff="src/main/java/resources/CAFETERIA_CHICA/PEDIDOS/";
        } else if (cafeteria.equals("Cafeteria_Grande")) {
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PEDIDO.csv";
            ffff="src/main/java/resources/CAFETERIA_GRANDE/PEDIDOS/";
        }
        try{

            Document doc=new Document();
            String path=new File(ffff+"pedido_").getCanonicalPath();
            FILE_NAME=path+ID+".pdf";
            PdfWriter.getInstance(doc,new FileOutputStream(new File(FILE_NAME)));
            doc.open();
            PdfPTable tabla=new PdfPTable(3);

            Paragraph incio=new Paragraph();
            Font fif=new Font();
            fif.setFamily(Font.FontFamily.TIMES_ROMAN.name());
            fif.setStyle(Font.BOLD);
            fif.setSize(15);
            Chunk ini=new Chunk(cafeteria,fif);
            incio.add(ini);
            incio.setAlignment(Element.ALIGN_CENTER);
            doc.add(incio);

            Paragraph enca=new Paragraph();
            Font ff=new Font();
            ff.setFamily(Font.FontFamily.TIMES_ROMAN.name());
            ff.setStyle(Font.BOLD);
            Chunk texto=new Chunk("Numero de pedido: "+ID,ff);
            enca.add(texto);
            enca.setAlignment(Element.ALIGN_LEFT);
            doc.add(enca);

            tabla.setWidthPercentage(80);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell("CANTIDAD");
            tabla.addCell("PRODUCTO");
            tabla.addCell("TOTAL INDIVIDUAL");

            try(BufferedReader br=new BufferedReader(new FileReader(filePath))){
                String linea;
                br.readLine();
                while((linea=br.readLine())!=null) {
                    String[] pedido=linea.split("\t");
                    tabla.addCell(pedido[1]);
                    if(pedido[3]==null){
                        tabla.addCell(pedido[0]);
                    }else if(pedido[3]!=null){
                        tabla.addCell(pedido[0]+" "+pedido[3]);
                    }else if(pedido[4]==null){
                        pedido[4]="0";
                    }

                    tabla.addCell("$"+pedido[2]);
                    total+=Integer.parseInt(pedido[2]);
                    tiempo+=Integer.parseInt(pedido[4]);
                    linea=null;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Paragraph encac=new Paragraph();
            Font fff=new Font();
            ff.setFamily(Font.FontFamily.TIMES_ROMAN.name());
            ff.setStyle(Font.BOLD);
            Chunk texto2=new Chunk("TIEMPO DE ESPERA: "+String.valueOf(tiempo)+" minutos",ff);
            encac.add(texto2+"\n");
            encac.setAlignment(Element.ALIGN_LEFT);

            Paragraph enca3=new Paragraph();
            enca3.add("\n\n");
            doc.add(encac);
            doc.add(enca3);
            doc.add(tabla);

            Paragraph encacc=new Paragraph();
            Font f2=new Font();
            f2.setFamily(Font.FontFamily.TIMES_ROMAN.name());
            f2.setStyle(Font.BOLD);
            Chunk texto3=new Chunk("Total a pagar: $"+String.valueOf(total),f2);
            encacc.add(texto3);
            encacc.setAlignment(Element.ALIGN_RIGHT);

            doc.add(encacc);
            doc.close();
            ddd(cafeteria);
        }catch (IOException | DocumentException e){
            e.printStackTrace();
        }

        return FILE_NAME;
    }



    public String ID(){
        Random r=new Random();
        int id= r.nextInt(200);
        String ID=String.valueOf(id);
        return ID;
    }

    public  void ddd(String cafeteria) {
        String filePath=null;
        if (cafeteria.equals("Cafeteria_chica")) {
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PEDIDO.csv";
        } else if (cafeteria.equals("Cafeteria_Grande")) {
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PEDIDO.csv";
        }

        try {
            List<String> encabezados = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            String encabezadosCSV = encabezados.get(0);
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(encabezadosCSV);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
