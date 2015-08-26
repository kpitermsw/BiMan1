package biman.middle.kokin.ru;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import biman.middle.kokin.ru.GPSConfirm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class ProcessHTTP /*extends Activity*/ {

	/** переменная для выполнения запроса к серверу */
  	private HttpClient client = new DefaultHttpClient();
  	/** документ XML  */	  	
  	private static Document doc;
  	/** класс для работы с XML  */
  	private XPath xpath = null ;
  	/** выражение XML  */	  	
  	private static XPathExpression expr;
  	/** результат выполнения выражения XML  */
  	private static Object resultEval;
  	/** список найденных узлов */
  	private static NodeList nodes;
  	
  	private HttpPost httpPost;      // HTTP соединение
  	HttpResponse response;			// response из POST

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int gpstype;
    Intent intent;
    Context context;
	LocationManager myManager;  // Менеджер служб GPS
	
  	
  	/**
  	 * Создаем соединение
  	 * @param sURL
  	 */
	public ProcessHTTP(String sURL) {

		httpPost = new HttpPost(sURL);

        //gpstype = preferences.getInt("JPStype", 0);
		}
	
	/**
	 * Отправляем POST запрос
	 */
	public void executePost() {

 
		try {
		if (context!=null) {
		try {
			if ((gpstype==0)||(gpstype==1)||(gpstype==2)) {
			myManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
			//Toast.makeText(context, "getStatusGPS()"+(myManager.isProviderEnabled("gps")?"1":"0"), Toast.LENGTH_LONG).show();
			
			if (!myManager.isProviderEnabled("gps")) {
				String messtoact = "GPS не включен";
				if (gpstype==0) {
					Toast.makeText(context , messtoact, Toast.LENGTH_LONG).show();
				} else {
					intent = new Intent(context, GPSConfirm.class);
					intent.putExtra("gpstype", gpstype);
					intent.putExtra("messtoact", messtoact);				
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					if (gpstype==2) {
						editor.putInt("gpsStop", 1);
						return;
					}
				}
			} else {
				if (preferences.getFloat("lat", 0)==0 ) {
					String messtoact = "Устройством не предоставлены GPS координаты";
					if (gpstype==0) {
						Toast.makeText(context, messtoact, Toast.LENGTH_LONG).show();
					} else {
						intent = new Intent(context, GPSConfirm.class);
						intent.putExtra("gpstype", gpstype);
						intent.putExtra("messtoact", messtoact);				
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						if (gpstype==2) {
							editor.putInt("gpsStop", 1);
							return;
						}
					}
				}
			}
			}
		} catch(Exception e) {
			System.out.println("getStatusGPS error!: "+e);
			return;
		}
		}
		
		
		try {
			//response = client.execute(httpPost);
		} catch(Exception e) {
			response = null;
			System.out.println("Post error!: "+e);
			Toast.makeText(context, "Post error!: "+e, Toast.LENGTH_LONG).show();
		}
		
		
    	} catch (Exception e) {
    		String se="ProcessHTTP:executePost: "+e;
    		System.out.println(se);
    		Toast.makeText(context, se, Toast.LENGTH_LONG).show(); 
    	}
		
	}

	/**
	 * Получить XML сообщение с сервера, подготовить к парсингу
	 * @param xmlContentToSend
	 * @param debug
	 * @return
	 */
	
	public XPath getXML0(String xmlContentToSend, int debug, SharedPreferences pref, Context cont) {
		return null;
	}
	
	public XPath getXML(String xmlContentToSend, int debug, SharedPreferences pref, Context cont) {
		//Toast.makeText(cont, "getXML 1", Toast.LENGTH_LONG).show();

		//CMTRLog cmtrlog = new CMTRLog(cont);
    	//cmtrlog.writeLog("xmlContentToSend=" + xmlContentToSend);

    	xpath = null;
		try {
			
			context = cont;
			preferences = pref;
			StringEntity entityXML = new StringEntity(xmlContentToSend, HTTP.UTF_8);
			entityXML.setContentType("application/xml");
			//Toast.makeText(context, "getXML 2", Toast.LENGTH_LONG).show();			

			httpPost.addHeader("Accept", "text/xml");
			httpPost.addHeader("Content-Type", "application/xml");
			httpPost.setEntity(entityXML);
			//Toast.makeText(context, "getXML 3", Toast.LENGTH_LONG).show();		
			gpstype = preferences.getInt("JPStype", 0); 
			
			try {
				executePost();
    		} catch (Exception e) {
        		String se="ProcessHTTP: 1 getXML: "+e;
        		System.out.println(se);
        		Toast.makeText(context, se, Toast.LENGTH_LONG).show(); 
    		}
			if (gpstype==2) {
				return null;
			}
			
			if (response!=null) {   
			
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					inputStream.mark(0);

					// Вывод XML
					
					if (debug==1) {
						int ch;
						String sss="";
						for (int total = 0; ( ch = inputStream.read()) != -1; total++) {
							sss +=(char)ch;
			        		}
			        
						System.out.println("sss="+sss);
						System.out.println("/sss");
					
						//inputStream = entity.getContent();
						inputStream.reset();
					}
					
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					factory.setNamespaceAware(true); // never forget this!
					DocumentBuilder builder = factory.newDocumentBuilder();
					doc = builder.parse(inputStream);
					XPathFactory xpfactory = XPathFactory.newInstance();
					xpath = xpfactory.newXPath();
			
				} catch (Exception e) {
		    		String se="ProcessHTTP: 4 getXML: responce=null!!!";
		    		System.out.println(se);
		    		Toast.makeText(cont, se, Toast.LENGTH_LONG).show(); 
					}
				}
			} else {
	    		String se="ProcessHTTP: 2 getXML: responce=null!!!";
	    		System.out.println(se);
	    		Toast.makeText(cont, se, Toast.LENGTH_LONG).show(); 
				
			}
			
			
    	} catch (Exception e) {
    		String se="ProcessHTTP: 3 getXML: "+e;
    		System.out.println(se);
    		Toast.makeText(cont, se, Toast.LENGTH_LONG).show(); 
    	}

		return xpath;
	}


	/**
	 * Количество узлов (mask) в документе
	 * @param mask
	 * @return
	 */
	public int countNodes(String mask) {
		try {
			expr = xpath.compile(mask);
			resultEval = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) resultEval;
		} catch (Exception e) {
			System.out.println(e);
		}
		return nodes.getLength();
	}

	/**
	 * Извлечь массив узлов 
	 * @param mask
	 * @return
	 */
    public String[] getNodesA(String mask) {
    	String[] ss={""};
    	try {
    		expr = xpath.compile(mask);
    		resultEval = expr.evaluate(doc, XPathConstants.NODESET);
    		nodes = (NodeList) resultEval;
    		ss=new String[nodes.getLength()];
    		for (int i = 0; i < nodes.getLength(); i++) {
    			ss[i]=nodes.item(i).getNodeValue();

	    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
	    
	    return ss;
	    
    }
	
	
  	/**
	 * Извлечь узел
  	 * @param mask маска поиска узлов
  	 */
    public String getNodes(String mask) {
    	String ss="";
    	try {
    		expr = xpath.compile(mask);
    		resultEval = expr.evaluate(doc, XPathConstants.NODESET);
    		nodes = (NodeList) resultEval;
    		for (int i = 0; i < nodes.getLength(); i++) {
    			ss+=nodes.item(i).getNodeValue();
 
	    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
	    
	    return ss;
	    
    }
	
	
}
