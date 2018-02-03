package com.example.sergio.gijondomindividual.dummy;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Helper class for providing sample categoria for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GestoraLugares extends ArrayList<Lugar>{

    /**
     * An array of sample (dummy) items.
     */
    //public static final List<Lugar> ITEMS = new ArrayList<Lugar>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Lugar> ITEM_MAP = new HashMap<String, Lugar>();



    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore descripcion information here.");
        }
        return builder.toString();
    }

    public GestoraLugares() {
        CargadoraDOM tarea=new CargadoraDOM(this);
        tarea.execute("http://datos.gijon.es/doc/turismo/turismo.xml");
    }

    class CargadoraDOM extends AsyncTask<String,Integer,Boolean> {
        private Node key=null;
        public CargadoraDOM(GestoraLugares gestora) {
            this.gestora = gestora;
        }
        private GestoraLugares gestora;
        private Document documento = null;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Boolean doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
                DocumentBuilder constructorDeDocumento = factoria.newDocumentBuilder();
                try (InputStream lector = url.openStream()) {
                    documento = constructorDeDocumento.parse(lector);

                }  catch (IOException ex) {
                    Logger.getLogger(GestoraLugares.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                } catch (SAXException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                //obtenemos el conjunto de elementos "asociacion"
                NodeList nodo = this.documento.getElementsByTagName("directorio");
                for (int x=0;x<nodo.getLength();x++) {
                    Lugar una = new Lugar(null,null,null,null,null,null);
                    //recorremos todos los elementos de una asociación
                    this.navigateTree(nodo.item(x), una);
                    //añadimos el objeto asociación al arrayList de asociaciones
                    gestora.add(una);
                }
                //if (gestora.size()>0 && MainActivity.adaptador!=null){
                    //MainActivity.adaptador.notifyDataSetChanged();
                //}
            }

        }

        private void navigateTree(Node n, Lugar una) {
            switch (n.getNodeType()) {
//        case Node.DOCUMENT_NODE:
//        {
//            Document doc=(Document) n;
//            System.out.println("Nodo tipo DOCUMENT_NODE"+n.getNodeName());
//        }
//            break;
                case Node.ELEMENT_NODE: {
                    this.key=n;
                    NodeList hijos = n.getChildNodes();
                    //Si el elemento contiene hijos hay que recorrerlos
                    if (hijos != null) {
                        for (int x = 0; x < hijos.getLength(); x++) {
                            this.navigateTree(hijos.item(x),una);
                        }
                    }
                }
                break;
                //si el nodo es de tipo texto puede ser información que nos interesa
                case Node.TEXT_NODE: {
                    this.procesarTexto(n, una);
                }
                break;
            }
        }
        private void procesarTexto(Node n, Lugar a) {
            String valor = n.getNodeValue();
            String nombreClave=this.key.getNodeName();
            //para poner en el objeto Directorio sólo la información que interesa
            if (a.containsKey(nombreClave) && a.get(nombreClave)==null){
                a.put(nombreClave, valor);
            }
        }
    }

}
