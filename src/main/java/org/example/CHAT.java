package org.example;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CHAT extends TelegramLongPollingBot {
    String estado=null;
    String nombreUser=null;
    String contraseñaUser=null;
    String cafeteria="";
    String presupuesto;
    String price;
    String producto;
    String cantidad;
    String tiempo;
    String guiso="";

    String cafeteriaPath;
    @Override
    public void onUpdateReceived(Update update) {
        String message=update.getMessage().getText();
        Long chatId=update.getMessage().getChatId();

           if(message.equals("/start")) {
               sendMessage(generateSenMessage(chatId, "BIENVENIDO A NUESTRO CHATBOT:\n Soy nombre_bot y estoy para ayudarle a realizar la compra de su almuerzo\n/iniciar_sesion(Solo para cafeterias)"));
               sendMessage(generateSenMessage(chatId, "Eliga donde desea comprar\n/Cafeteria_Grande\n/Cafeteria_chica\n/Productos_con_presupuesto"));
           }else if (message.startsWith("/iniciar_sesion")) {
               estado="nombre";
               sendMessage(generateSenMessage(chatId, "Ingrese nombre del encargado "));
           } else if (message.equals("/Cafeteria_chica")) {
               cafeteria="Cafeteria_chica";
               cafeteriaPath="CAFETERIA_CHICA";
               sendMessage(generateSenMessage(chatId, "Menú de la Cafeteria_chica\nSeleccione su producto: "));
               Find_users find=new Find_users();

               String prod=find.FindProductos("src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv");
               String []productos=prod.split("\t");
               String prec=find.Findprecios("src/main/java/resources/CAFETERIA_CHICA/PRODUCTOS.csv");
               String[]precios=prec.split("\t");
               sendMessage(generateSenMessage(chatId,"PRODUCTOS:"));
               for(int i=0;i<productos.length;i++){
                   sendMessage(generateSenMessage(chatId,"/"+productos[i]+ " $"+precios[i]));
                   estado="eleccionMenuCh";
               }

               String comidaS=find.FindComidas("src/main/java/resources/CAFETERIA_CHICA/COMIDAS.csv");
               productos=comidaS.split("\t");
               String precc= find.Findprecios("src/main/java/resources/CAFETERIA_CHICA/COMIDAS.csv");
               precios=precc.split("\t");
               sendMessage(generateSenMessage(chatId,"Platillos:"));
               for(int i=0;i<productos.length;i++){
                   sendMessage(generateSenMessage(chatId,"/"+productos[i]+" $"+precios[i]));
                   estado="eleccionMenuCh";
               }

               String comidasGuiso=find.findmenuComidasGuiso("src/main/java/resources/CAFETERIA_CHICA/COMIDAS_CON_GUISO.csv");
               String[] CCG=comidasGuiso.split("\t");
               String pp=find.Findprecios("src/main/java/resources/CAFETERIA_CHICA/COMIDAS_CON_GUISO.csv");
               String[] ppG=pp.split("\t");
               for(int i=0;i< ppG.length;i++){
                   sendMessage(generateSenMessage(chatId,"/"+CCG[i]+" $"+ppG[i]));
                   estado="eleccionMenuCh";
               }

           }else if (message.equals("/Cafeteria_Grande")) {
               cafeteria="Cafeteria_Grande";
               cafeteriaPath="CAFETERIA_GRANDE";
               sendMessage(generateSenMessage(chatId, "Menú de la Cafeteria_Grande:\n"));
               Find_users find2=new Find_users();

               String prodG=find2.FindProductos("src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv");
               String []productosG=prodG.split("\t");
               String precG=find2.Findprecios("src/main/java/resources/CAFETERIA_GRANDE/PRODUCTOS.csv");
               String[]preciosG=precG.split("\t");
               sendMessage(generateSenMessage(chatId,"PRODUCTOS:"));
               for(int i=0;i<productosG.length;i++){
                   sendMessage(generateSenMessage(chatId,"/"+productosG[i]+ " $"+preciosG[i]));
                   estado="eleccionMenuCh";
               }

               String comidaSG=find2.FindComidas("src/main/java/resources/CAFETERIA_GRANDE/COMIDAS.csv");
               String[] productosGG=comidaSG.split("\t");
               String preccGG= find2.Findprecioss("src/main/java/resources/CAFETERIA_GRANDE/COMIDAS.csv");
               String[] preciosGG=preccGG.split("\t");
               sendMessage(generateSenMessage(chatId,"Platillos:"));
               for(int i=0;i<productosGG.length;i++){
                   sendMessage(generateSenMessage(chatId,"/"+productosGG[i]+" $"+preciosGG[i]));
                   estado="eleccionMenuCh";
               }

               String comidasGuisoG=find2.findmenuComidasGuiso("src/main/java/resources/CAFETERIA_GRANDE/COMIDAS_CON_GUISO.csv");
               String[] CCGG=comidasGuisoG.split("\t");
               String ppG=find2.Findprecioss("src/main/java/resources/CAFETERIA_GRANDE/COMIDAS_CON_GUISO.csv");
               String[] ppGG=ppG.split("\t");
               for(int i=0;i< ppGG.length;i++){
                   sendMessage(generateSenMessage(chatId,"/"+CCGG[i]+" $"+ppGG[i]));
                   estado="eleccionMenuCh";
               }
           } else if (message.equals("/Productos_con_presupuesto")) {
               sendMessage(generateSenMessage(chatId, "Ingrese el presupuesto: "));
               estado="presupuesto";

           }else if(message.equals("/CONFIRMAR")){
               sendMessage(generateSenMessage(chatId,"Se ha enviado su pedido"));
               Find_users ingresar=new Find_users();
               Boolean modificar= ingresar.disposicion(producto,cafeteria,Integer.parseInt(cantidad));
               ingresar.pedido(producto,cantidad,price,cafeteria,guiso,tiempo);
               Pedido pp=new Pedido();
               try {
                   String pdfFile=pp.pdf(cafeteria);
                   sendMessage(generateSendFile(chatId,pdfFile));
                   String IDs=ingresar.findUser(cafeteria);
                   sendMessage(generateSendFile(Long.valueOf(IDs),pdfFile));

               } catch (IOException | DocumentException e) {
                   throw new RuntimeException(e);
               }
               producto=null;
               cantidad=null;
               price=null;
               guiso=null;
               tiempo=null;
               cafeteria=null;
               cafeteriaPath=null;
           }else if(message.equals("/Seleccionar_otro")){
               System.out.println(producto+" "+cantidad+" "+price+" "+cafeteria+" "+guiso+tiempo);
               Find_users ingresar=new Find_users();
               Boolean modificar= ingresar.disposicion(producto,cafeteria,Integer.parseInt(cantidad));
               ingresar.pedido(producto,cantidad,price,cafeteria,guiso,tiempo);
               producto=null;
               cantidad=null;
               price=null;
               guiso=null;
               estado="/"+cafeteria;
               tiempo=null;
               sendMessage(generateSenMessage(chatId,"Seleccione otro productos de la misma cafeteria: /"+cafeteria));

           }else if(estado.equals("nombre")){
               nombreUser=message;
               sendMessage(generateSenMessage(chatId, " Ingresa la contraseña"));
               estado="contraseña";
           }else if(estado.equals("contraseña")){
               contraseñaUser=message;
               sendMessage(generateSenMessage(chatId,"Selecciona la cefeteria\n/1 Cafeteria_Grande"+ "\n/2 Cafeteria_chica"));
               estado="cafeteria";
           }else if(estado.equals("cafeteria")){
               Find_users find=new Find_users();
               String ID=chatId.toString();
               if(message.equals("/1")){
                   cafeteria="Cafeteria_Grande";

                  Find_users ff=new Find_users();
                  if(!ff.buscando(contraseñaUser,nombreUser,cafeteria,ID)){
                      sendMessage(generateSenMessage(chatId,"Contraseña y/o usuario incorrecto"));
                      sendMessage(generateSenMessage(chatId,"/start"));
                  }
                   estado=" ";
               }else if(message.equals("/2")){
                   cafeteria="Cafeteria_chica";
                   Find_users ff=new Find_users();
                   if(!ff.buscando(contraseñaUser,nombreUser,cafeteria,ID)){
                       sendMessage(generateSenMessage(chatId,"Contraseña y/o usuario incorrecto"));
                       sendMessage(generateSenMessage(chatId,"/start"));
                   }
                   estado=" ";
               }
               sendMessage(generateSenMessage(chatId,"/start"));
           }else if(estado.equals("eleccionMenuCh")){

               Find_users find=new Find_users();
               String prod=find.FindProductos("src/main/java/resources/"+cafeteriaPath+"/PRODUCTOS.csv");
               String []productos=prod.split("\t");
               String prec=find.Findprecios("src/main/java/resources/"+cafeteriaPath+"/PRODUCTOS.csv");
               String[]precios=prec.split("\t");
               for(int i=0;i<productos.length;i++){
                  if(message.equals("/"+productos[i])) {
                      Find_users dispon=new Find_users();
                      if(dispon.disposicion(productos[i],cafeteria)){
                          producto = productos[i];
                          price=precios[i];
                          sendMessage(generateSenMessage(chatId, "Ingresa la cantidad:"));
                          estado="cantidad";
                          tiempo="0";
                      }else{
                          sendMessage(generateSenMessage(chatId,"Producto agotado. Seleccione otro producto\n/"+cafeteria));
                      }
                  }
               }
              Find_users buscar=new Find_users();
               String comidas=buscar.FindComidas("src/main/java/resources/"+cafeteriaPath+"/COMIDAS.csv");
               String[] comida=comidas.split("\t");
               String precio_comida=buscar.Findprecios("src/main/java/resources/"+cafeteriaPath+"/COMIDAS.csv");
               String[] precio_c=precio_comida.split("\t");
               String tiempoComida=buscar.Findtiempo("src/main/java/resources/"+cafeteriaPath+"/COMIDAS.csv");
               String []Tiempo=tiempoComida.split("\t");
               for(int i=0;i<comida.length;i++){
                   if(message.equals("/"+comida[i])) {
                       producto=comida[i];
                       price=precio_c[i];
                       tiempo=Tiempo[i];
                       sendMessage(generateSenMessage(chatId, "Ingresa la cantidad:"));
                       estado="cantidad";
                   }
               }
               Find_users bb=new Find_users();
               String comidaC=bb.FindComidas("src/main/java/resources/"+cafeteriaPath+"/COMIDAS_CON_GUISO.csv");
               String[] ccccc=comidaC.split("\t");
               String prr=bb.Findprecios("src/main/java/resources/"+cafeteriaPath+"/COMIDAS_CON_GUISO.csv");
               String[] rrr=prr.split("\t");
               String PCG=bb.Findtiempo("src/main/java/resources/"+cafeteriaPath+"/COMIDAS_CON_GUISO.csv");
               String[] pcg=PCG.split("\t");
               for(int i=0;i<ccccc.length;i++){
                   if(message.equals("/"+ccccc[i])) {
                       producto=ccccc[i];
                       price=rrr[i];
                       tiempo=pcg[i];
                       sendMessage(generateSenMessage(chatId, "Seleccione el Guiso: \n/1 Salsa verde\n/2 Picadillo\n/3 Chicharron\n/4 Deshebrada"));
                       estado="GUISO";
                   }
               }
           }else if(estado.equals("cantidad")){
               cantidad=message;
               int ca=Integer.parseInt(cantidad);
               int p=Integer.parseInt(price);
               int rr=ca*p;
               price=String.valueOf(rr);
               p=Integer.parseInt(tiempo);
               rr=p*ca;
               tiempo=String.valueOf(rr);
               sendMessage(generateSenMessage(chatId,"Confirme su pedido:\n/CONFIRMAR\n/Seleccionar_otro"));

           }else if(estado.equals("GUISO")){
               if(message.equals("/1")){

               }else if(message.equals("/1")){
                   guiso="Salsa verde";
               }else if(message.equals("/2")){
                   guiso="Picadillo";
               }else if(message.equals("/3")){
                   guiso="Chicharron";
               }else if(message.equals("/4")){
                   guiso="Deshebrada";
               }
               sendMessage(generateSenMessage(chatId,"Ingresa la cantidad:"));
               estado="cantidad";
           } else if (estado.equals("presupuesto")) {
               presupuesto=message;
               sendMessage(generateSenMessage(chatId,"Ver opciones de la Cafeteria Grande /presupuesto_CGrande\n/presupuesto_Cchica"));
               estado="opciondepresupuesto";
           }else if (estado.equals("presupuestoMenu")) {
               sendMessage(generateSenMessage(chatId,"Opciones que puede comprar:"));
               String pres = presupuesto;
               sendMessage(generateSenMessage(chatId, pres));
               Find_users finddd = new Find_users();
               String prodd = finddd.FindProductos("src/main/java/resources/" + cafeteriaPath + "/PRODUCTOS.csv");
               String[] productoss = prodd.split("\t");
               String precc = finddd.Findprecios("src/main/java/resources/" + cafeteriaPath + "/PRODUCTOS.csv");
               String[] precioss = precc.split("\t");
               for (int i = 0; i < productoss.length; i++) {
                   if (Integer.parseInt(precioss[i]) < Integer.parseInt(pres)) {
                       sendMessage(generateSenMessage(chatId, "/" + productoss[i] + " $" + precioss[i]));
                   }
               }
               Find_users buscar = new Find_users();
               String comidas = buscar.FindComidas("src/main/java/resources/" + cafeteriaPath + "/COMIDAS.csv");
               String[] comida = comidas.split("\t");
               String precio_comida = buscar.Findprecios("src/main/java/resources/" + cafeteriaPath + "/COMIDAS.csv");
               String[] precio_c = precio_comida.split("\t");
               String tiempoComida = buscar.Findtiempo("src/main/java/resources/" + cafeteriaPath + "/COMIDAS.csv");
               String[] Tiempo = tiempoComida.split("\t");
               for (int i = 0; i < comida.length; i++) {
                   if (Integer.parseInt(precio_c[i]) < Integer.parseInt(pres)) {
                       sendMessage(generateSenMessage(chatId, "/" + comida[i] + " $" + precio_c[i]));
                   }
               }
               Find_users bb = new Find_users();
               String comidaC = bb.FindComidas("src/main/java/resources/" + cafeteriaPath + "/COMIDAS_CON_GUISO.csv");
               String[] ccccc = comidaC.split("\t");
               String prr = bb.Findprecioss("src/main/java/resources/" + cafeteriaPath + "/COMIDAS_CON_GUISO.csv");
               String[] rrr = prr.split("\t");
               String PCG = bb.Findtiempo("src/main/java/resources/" + cafeteriaPath + "/COMIDAS_CON_GUISO.csv");
               String[] pcg = PCG.split("\t");
               for (int i = 0; i < ccccc.length; i++) {
                   if (Integer.parseInt(rrr[i]) < Integer.parseInt(pres)) {
                       sendMessage(generateSenMessage(chatId, "/" + ccccc[i] + " $" + rrr[i]));
                   }
               }
               estado="eleccionMenuCh";
           } else if(estado.equals("opciondepresupuesto")){
               if(message.equals("/presupuesto_CGrande")){
                   cafeteriaPath = "CAFETERIA_GRANDE";
                   cafeteria="Cafeteria_Grande";
                   estado="presupuestoMenu";
               }else if(message.equals("/presupuesto_Cchica")){
                   cafeteriaPath="CAFETERIA_CHICA";
                   cafeteria="Cafeteria_chica";
                   estado="presupuestoMenu";
               }
               estado="presupuestoMenu";
           }
    }



    private SendMessage generateSenMessage(Long chatId, String opciones) {
        return new SendMessage(chatId.toString(), opciones);
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendDocument generateSendFile(Long chatId, String archivo) throws FileNotFoundException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId.toString());
        File file = new File(archivo);
        InputFile doc = new InputFile(file);
        sendDocument.setDocument(doc);
        return sendDocument;
    }

    private void sendMessage(SendDocument sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "cafe_upv_bot";
    }

    @Override
    public String getBotToken() {
        return "7107423484:AAEQPsADE1K89EI-zQe0_eEOeXltmimW-_Q";
    }
}
