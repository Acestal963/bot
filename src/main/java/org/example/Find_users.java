package org.example;
import org.apache.commons.csv.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Find_users {

    public String findUser (String cafeteria){
        String id = null;
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/resources/ENCARGADOS.csv"))) {
            String linea;
            br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\t");
                System.out.println(datos[2]);
                if (datos[3].equals(cafeteria) && datos[2]!=null) {
                    id = datos[2];
                    System.out.println("ID encontrado: " + id);
                    return id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean buscando (String contraseña, String nombre, String cafeteria,String Id){
            String filepath="src/main/java/resources/ENCARGADOS.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String linea=null;
            while((linea=br.readLine())!=null){
                String[]datos=linea.split("\t");
                if(datos[0].equals(nombre)&&datos[1].equals(contraseña)&&datos[3].equals(cafeteria)){
                    agregarGerente(nombre,contraseña,Id,cafeteria);
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public boolean agregarGerente(String nombre, String contraseña,String id,String cafeteria){
            String filePath = "src/main/java/resources/ENCARGADOS.csv";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
                writer.write(nombre+"\t"+contraseña+"\t"+id+"\t"+cafeteria);
                writer.newLine();
            } catch (IOException e) {

            }
        return true;
    }

    public String findmenuComidasGuiso(String c){
        String prod=null;
        try (BufferedReader br = new BufferedReader(new FileReader(c))) {

            prod= br.readLine();
            return prod;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String FindComidas(String caf){
        String prod=null;
        try (BufferedReader br = new BufferedReader(new FileReader(caf))) {
            prod= br.readLine();
            return prod;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String FindProductos(String Cafeteria){
        String prod=null;
        try (BufferedReader br = new BufferedReader(new FileReader(Cafeteria))) {
            prod= br.readLine();
            return prod;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String Findprecios(String precio){
        String prod=null;
        try (BufferedReader br = new BufferedReader(new FileReader(precio))) {
            br.readLine();
            prod= br.readLine();
            return prod;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String Findprecioss(String precio){
        String prod=null;
        try (BufferedReader br = new BufferedReader(new FileReader(precio))) {
            br.readLine();
            prod= br.readLine();
            return prod;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String Findtiempo(String Cafeteria){
        String prod=null;
        try (BufferedReader br = new BufferedReader(new FileReader(Cafeteria))) {
            br.readLine();
            br.readLine();

            prod= br.readLine();
            return prod;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void pedido(String producto, String cantidad, String price, String cafeteria, String guiso,String tiempo){
        String filePath=null;
        if(cafeteria.equals("Cafeteria_chica")){
             filePath = "src/main/java/resources/CAFETERIA_CHICA/PEDIDO.csv";
        }else if(cafeteria.equals("Cafeteria_Grande")){
             filePath = "src/main/java/resources/CAFETERIA_GRANDE/PEDIDO.csv";
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
            String []datos = new String[0];
            writer.write(producto+"\t"+cantidad+"\t"+price+"\t"+guiso+"\t"+tiempo);
            writer.newLine();
        } catch (IOException e) {

        }
    }



    public int posicion(String producto, String cafeteria){
        String filePath=null;
        if(cafeteria.equals("Cafeteria_chica")){
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv";
        }else if(cafeteria.equals("Cafeteria_Grande")){
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv";
        }
        String linea;
        int  poss=0;
        try(BufferedReader br=new BufferedReader(new FileReader(filePath))){
                linea=br.readLine();
                String[]datos=linea.split("\t");
                for(int i=0;i<datos.length;i++){
                    if(datos[i].equals(producto)){
                        return i;
                    }
                }
                return poss;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean disposicion(String producto, String cafeteria){

        String filePath=null;
        if(cafeteria.equals("Cafeteria_chica")){
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv";
        }else if(cafeteria.equals("Cafeteria_Grande")){
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv";
        }
        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            linea= br.readLine();
            String[] datos=linea.split("\t");
            int pos= posicion(producto,cafeteria);
            int cantidad=Integer.parseInt(cant(producto,cafeteria));
            System.out.println(pos+cantidad+producto);
            if(cantidad>0){
                //modificardis(pos,cafeteria,producto);
                return true;
            }else
            {
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean disposicion(String producto, String cafeteria, int ca){

        String filePath=null;
        if(cafeteria.equals("Cafeteria_chica")){
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv";
        }else if(cafeteria.equals("Cafeteria_Grande")){
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv";
        }
        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            linea= br.readLine();
            String[] datos=linea.split("\t");
            int pos= posicion(producto,cafeteria);
            int cantidad=Integer.parseInt(cant(producto,cafeteria));

            if(cantidad>0){
                modificardis(pos,cafeteria,producto,ca);
                return true;
            }else
            {
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void modificardis(int pos, String cafeteria, String producto,int cant){
        String filePath=null;
        if(cafeteria.equals("Cafeteria_chica")){
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv";
        }else if(cafeteria.equals("Cafeteria_Grande")){
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv";
        }

        int filaModificar = 2;
        int columnaModificar = pos;

        int nuevoValor = Integer.parseInt(cant(producto,cafeteria))-cant;
        System.out.println(nuevoValor);
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

            String lineaModificar = lines.get(filaModificar);

            String[] partes = lineaModificar.split("\t");

            partes[columnaModificar] = String.valueOf(nuevoValor);

            String nuevaLinea = String.join("\t", partes);

            lines.set(filaModificar, nuevaLinea);

            Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String cant(String producto, String cafeteria){

        String filePath=null;
        if(cafeteria.equals("Cafeteria_chica")){
            filePath = "src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv";
        }else if(cafeteria.equals("Cafeteria_Grande")){
            filePath = "src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv";
        }
        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            br.readLine();
            linea= br.readLine();
            String[] datos=linea.split("\t");
            int pos=posicion(producto,cafeteria);
            return datos[pos];

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
