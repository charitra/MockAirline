package com.mindtree.tth.hackathon.airbooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class AirBookingServlet extends HttpServlet {
	
	
	
	@Override
    protected void service(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws ServletException,
        IOException {
		
		String response = null;
        System.out.println("Method: "+httpRequest.getMethod());        
        
        try {
			if (httpRequest.getContentLength() > 0) {
				getRootElement(httpRequest.getInputStream());				
				String availResXml = readAvailResponseXml();
				httpResponse.getOutputStream().write(availResXml.getBytes());
			}
			else {
				httpResponse.sendError(HttpServletResponse.SC_NO_CONTENT);
			}
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
        System.out.println("In service method..");
        System.out.println("-------------------------------------------");
    }
	
	private void getRootElement(InputStream is) {
		 DocumentBuilderFactory dbFactory 
         = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder;

      try {
		dBuilder = dbFactory.newDocumentBuilder();

		  Document doc = dBuilder.parse(is);
		  doc.getDocumentElement().normalize();
		  System.out.println(doc.getDocumentElement().getNodeName());
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      XPath xPath =  XPathFactory.newInstance().newXPath();

	}
	
	/**
	 * The method reads the availability response xml.
	 * @param httpReq
	 * @param httpRes
	 */
	private String readAvailResponseXml() {
		InputStream is = null;
		BufferedReader buffRdr = null;
		String xmlString = null;
		try {			
			is = getClass().getClassLoader().getResourceAsStream("response/avail-res.xml");
			xmlString = getXmlString(is);
								
		}
		finally {			
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return xmlString;
	}
	
	private String getXmlString(InputStream is) {
		BufferedReader buffRdr = null;
		StringBuilder availResBldr = new StringBuilder();
		try {			
			
			buffRdr = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			while ((line = buffRdr.readLine()) != null) {
				availResBldr.append(line);
			}						
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		finally {
			if (buffRdr != null) {
				try {
					buffRdr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
		return availResBldr.toString();
	}
}
