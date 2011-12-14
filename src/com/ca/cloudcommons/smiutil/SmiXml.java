package com.ca.cloudcommons.smiutil;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SmiXml {

	static Logger _log = Logger.getLogger(SmiXml.class);

	private ArrayList<ProviderInfo> providers = new ArrayList<ProviderInfo>();

	public SmiXml() {
	}

	void loadProviderList(Document doc) {
		providers = createProviderList(doc);
	}

	ArrayList<ProviderInfo> createProviderList(Document doc) {
		ArrayList<ProviderInfo> al = new ArrayList<ProviderInfo>();

		_log.info("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("result");

		nList = getElements(nList, "Bean-List");
		nList = getElements(nList, "ResourceProvider-Bean");

		_log.info("-----------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				_log.info("provider : " + getTagValue("name", eElement) + " uuid : " + getTagValue("uuid", eElement));
				// System.out.println("");
				al.add(new ProviderInfo(getTagValue("name", eElement), getTagValue("uuid", eElement), ""));
			}
		}

		return al;
	}

	ProviderInfo getProvider(String name) throws Exception {
		ProviderInfo retval = null;

		for (ProviderInfo providerInfo : providers) {

			if (providerInfo.getName().equalsIgnoreCase(name)) {
				retval = providerInfo;
				break;
			}
		}

		return retval;
	}

	NodeList getElements(NodeList nList, String value) {
		Node nNode = nList.item(0);
		Element eElement = (Element) nNode;

		// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		//
		// }

		return eElement.getElementsByTagName(value);
	}

	private String getTagValue(String sTag, Element eElement) {
		String retval = "";

		NodeList tagElemnts = eElement.getElementsByTagName(sTag);

		if (tagElemnts.getLength() > 0) {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

			Node nValue = (Node) nlList.item(0);

			retval = nValue.getNodeValue();
		}

		return retval;
	}

	public class ProviderInfo {
		private String name, uuid, description;

		public ProviderInfo(String name, String uuid, String description) {
			super();
			this.name = name;
			this.uuid = uuid;
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

}
