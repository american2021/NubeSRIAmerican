/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NUBE;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Jaime
 */
public class enviarCorreo {

    public boolean enviarCorreo(String archivo, String clave, String correo) {
//        correo = "facturascoradac@hotmail.com";
        if (correo != null) {
            if (correo.length() > 0) {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.setProperty("mail.smtp.starttls.enable", "true");
                    props.setProperty("mail.smtp.port", "587");
                    props.setProperty("mail.smtp.user", "facturascoradac@gmail.com");
                    props.put("mail.smtp.clave", "hhmqhkldhbwilhrh");
                    props.setProperty("mail.smtp.auth", "true");

                    Session session = Session.getDefaultInstance(props, null);
                    // session.setDebug(true);

                    // Se compone la parte del texto
                    BodyPart texto = new MimeBodyPart();
                    texto.setText("Se adjunta su factura en formato xml y el RIDE. Saludos. Por favor no responda.");

                    // Se compone el adjunto con la imagen
                    BodyPart adjunto = new MimeBodyPart();
                    adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo + clave + ".xml")));
                    adjunto.setFileName(clave + ".xml");

                    // Una MultiParte para agrupar texto e imagen.
                    MimeMultipart multiParte = new MimeMultipart();
                    multiParte.addBodyPart(texto);
                    multiParte.addBodyPart(adjunto);

                    adjunto = new MimeBodyPart();
                    adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo + "RIDE\\" + clave + ".pdf")));
                    adjunto.setFileName(clave + ".pdf");

                    multiParte.addBodyPart(adjunto);

                    // Se compone el correo, dando to, from, subject y el
                    // contenido.
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("facturascoradac@gmail.com"));
                    message.addRecipient(
                            Message.RecipientType.TO,
                            new InternetAddress(correo));
                    message.setSubject("Comprobante Electronico generado");
                    message.setContent(multiParte);

                    // Se envia el correo.
                    Transport t = session.getTransport("smtp");
                    t.connect("facturascoradac@gmail.com", "hhmqhkldhbwilhrh");
                    t.sendMessage(message, message.getAllRecipients());
                    t.close();
                    System.out.println("enviado al correo");
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
