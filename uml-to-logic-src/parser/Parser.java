package parser;

import graph.*;

import java.io.*;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import translator.umlassociations.UMLAssociation;
import translator.umlassociations.UMLIsaAssociation;
import translator.umlclasses.UMLAttribute;
import translator.umlclasses.UMLClass;
import translator.umlclasses.UMLOperation;
import translator.umlgeneralizations.UMLGeneralization;
import translator.umlkeyconstraint.UMLKeyConstraint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	private static final Pattern pAttribute = Pattern.compile("\\s*(\\w+)\\s*:\\s*(\\w+)\\s*(\\{(\\d+).{2,3}(\\d+|\\*)\\})?");
	private static final Pattern pOperationName = Pattern.compile("(\\w+)\\s*");
	private static final Pattern pOperationAttrib = Pattern.compile("\\s*(\\w+)\\s*:\\s*(\\w+)");
	private static final Pattern pOperationDomain = Pattern.compile("\\s*:\\s*(\\w+)");
	private static final Pattern pName = Pattern.compile("\\w+");
	private static final Pattern pMulteplicity = Pattern.compile("\\((\\d+).{2,3}(\\d+|\\*)\\)");
	private static final Pattern pDisjoint = Pattern.compile(".*[Dd]isjoint.*");
	private static final Pattern pComplete = Pattern.compile(".*[Cc]omplete.*");
	private static final String operationSplit = "[\\(\\),]";
	
	private String xmlFileName;
	private PrintStream formulaePrintStream;
	private HashMap<String,UMLClassNode> umlClassNodes;
	private HashMap<String,DiamondNode> diamondNodes;
	private HashMap<String,EllipseNode> ellipseNodes;
	private HashMap<String,RectangleNode> rectangleNodes;
	
	public Parser(String xmlFileName, String formulaeFileName) {
		umlClassNodes = new HashMap<String,UMLClassNode>();
		diamondNodes = new HashMap<String,DiamondNode>();
		ellipseNodes = new HashMap<String,EllipseNode>();
		rectangleNodes = new HashMap<String,RectangleNode>();
		
		this.xmlFileName = xmlFileName;

		try {
			if(formulaeFileName == null)
				formulaePrintStream = System.out;
			else{
				FileOutputStream fstream = new FileOutputStream(formulaeFileName);
				formulaePrintStream = new PrintStream(fstream);
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = dbf.newDocumentBuilder();
			File xmlFile = new File(this.xmlFileName);
			Document doc = builder.parse(xmlFile);
			
			
			makeGraph(doc);
			parseUMLClasses();
			this.parseUMLAssociation();
			this.parseUMLGeneralization();
			this.parseUMLKeyConstraint();
			
				
		} catch (SAXException sxe) {
		} catch (ParserConfigurationException pce) {
			System.out.println("Parsing problems detected.");
		} catch (IOException ioe) {
			System.out.println("Riding or writing problems detected.");
		} 
//		catch (Exception e){
//			System.out.println("Unknown problem detected.");
//		}
	}
	private void makeGraph(Document doc){
		NodeList nodes = doc.getElementsByTagName("node");
		NodeList edges = doc.getElementsByTagName("edge");
		for (int i = 0; i < nodes.getLength(); i++){
			Element node = (Element)nodes.item(i);
			String id = node.getAttribute("id");
			String name = getLabel(node,"y:NodeLabel");
			if(node.getElementsByTagName("y:UMLClassNode").getLength() != 0){	
				String attributes = getLabel(node,"y:AttributeLabel");
				String operations = getLabel(node,"y:MethodLabel");
				umlClassNodes.put(id, new UMLClassNode(name, attributes, operations));
			}
			else if (node.getElementsByTagName("y:ShapeNode").getLength() != 0){
				Element shape = (Element)node.getElementsByTagName("y:Shape").item(0);
				String shapeType = shape.getAttribute("type");
				if(shapeType.equals("rectangle")){
					String label = getLabel(node,"y:NodeLabel");
					rectangleNodes.put(id, new RectangleNode(name,label));
				}
				else if(shapeType.equals("diamond")){
					diamondNodes.put(id, new DiamondNode(name));
				}
				else if(shapeType.equals("ellipse")){
					ellipseNodes.put(id, new EllipseNode(name));
				}
			}
		}
		for (int i = 0; i < edges.getLength(); i++){
			Element edgeElem = (Element)edges.item(i);
			String source = edgeElem.getAttributes().getNamedItem("source").getTextContent();
			String target = edgeElem.getAttributes().getNamedItem("target").getTextContent();
			Node sourceNode = getNode(source);
			Node targetNode = getNode(target);
			Edge edge = null;
			if (edgeElem.getElementsByTagName("y:PolyLineEdge").getLength() != 0){
				Element arrow = (Element)edgeElem.getElementsByTagName("y:Arrows").item(0);
				if(arrow.getAttribute("target").equals("standard") || arrow.getAttribute("target").equals("white_delta")){
					edge = new ArrowEdge(sourceNode,targetNode);
				}
				else {
					String[] labels = getLabels(edgeElem,"y:EdgeLabel");
					edge = new NormalEdge(sourceNode,targetNode,labels);
				}
			}
			if (edgeElem.getElementsByTagName("y:ArcEdge").getLength() != 0){
				edge = new ArchEdge(sourceNode,targetNode);
			}
			sourceNode.addEdge(edge);
			targetNode.addEdge(edge);
		}
	}
	private Node getNode(String id){
		Node node = diamondNodes.get(id);
		if(node == null)
			node = umlClassNodes.get(id);
		if(node == null)
			node = rectangleNodes.get(id);
		if(node == null)
			node = ellipseNodes.get(id);
		return node;
	}
	public void parseUMLClasses() throws SAXException,ParserConfigurationException{
		Iterator<UMLClassNode> iter = umlClassNodes.values().iterator();
		while(iter.hasNext()){
			UMLClassNode node = iter.next();
			UMLClass umlClass = new UMLClass(node.getName());
			if(node.getAttributesLabel() != ""){
				String[] attributes = node.getAttributesLabel().split("\n");
				for(int j = 0; j < attributes.length; j++){
					umlClass.addAttribute(this.parseUMLAttribute(attributes[j], umlClass));
			   }
			}
			if(node.getOperationsLable() != ""){
				String[] operations = node.getOperationsLable().split("\n");
				for(int j = 0; j < operations.length; j++){
					umlClass.addOperation(this.parseUMLOperation(operations[j], umlClass));
				}
			}

			writeFormulae(umlClass.translateFOL());
			writeFormulae(umlClass.translateDL());
	   }
	}
	public void parseUMLAssociation() throws SAXException,ParserConfigurationException{
		Iterator<DiamondNode> iter = diamondNodes.values().iterator();
		while (iter.hasNext()){
			DiamondNode node = iter.next();		
			UMLAssociation umlAssociation = new UMLAssociation();	
			umlAssociation.setName(node.getName());
			
			ArrayList<NormalEdge> edges = node.getNormalEdges();
			String[] labels = edges.get(0).getLabels();
			for(int k = 0; k < labels.length; k++){
				Matcher matchMult = pMulteplicity.matcher(labels[k]);
				if(matchMult.matches()){
					umlAssociation.setSecondMultiplicityMin(matchMult.group(1));
					umlAssociation.setSecondMultiplicityMax(matchMult.group(2));
				}
				else{
					Matcher matchName = pName.matcher(labels[k]);
					if(matchName.matches()) {
						umlAssociation.setFirstRole(matchName.group(0));
					}
				}
			}
			labels = edges.get(1).getLabels();
			for(int k = 0; k < labels.length; k++){
				Matcher matchMult = pMulteplicity.matcher(labels[k]);
				if(matchMult.matches()){
					umlAssociation.setFirstMultiplicityMin(matchMult.group(1));
					umlAssociation.setFirstMultiplicityMax(matchMult.group(2));
				}
				else{
					Matcher matchName = pName.matcher(labels[k]);
					if(matchName.matches()) {
						umlAssociation.setSecondRole(matchName.group(0));
					}
				}
			}
			String nameFirstClass = null;
			if (edges.get(0).getSource().equals(node))
				 nameFirstClass = edges.get(0).getTarget().getName();
			else
				nameFirstClass = edges.get(0).getSource().getName();
			umlAssociation.setNameFirstClass(nameFirstClass);
			String nameSecondClass = null;
			if (edges.get(1).getSource().equals(node))
				 nameSecondClass = edges.get(1).getTarget().getName();
			else
				nameSecondClass = edges.get(1).getSource().getName();
			umlAssociation.setNameSecondClass(nameSecondClass);
			
			ArrayList<ArrowEdge> arrowEdge = node.getArrowEdges();
			for(int i = 0; i < arrowEdge.size(); i++){
				if (arrowEdge.get(i).getSource().equals(node)){
					UMLIsaAssociation isa = new UMLIsaAssociation(arrowEdge.get(i).getTarget().getName(), node.getName());
					writeFormulae(isa.translateFOL());
					writeFormulae(isa.translateDL());
				}
			}
			writeFormulae(umlAssociation.translateFOL());
			writeFormulae(umlAssociation.translateDL());			
		}
	}
	public void parseUMLGeneralization() throws SAXException,ParserConfigurationException{
		Iterator<RectangleNode> iter = this.rectangleNodes.values().iterator();
		while (iter.hasNext()){
			RectangleNode node = iter.next();
			String fatherName = node.getArrowEdges().get(0).getTarget().getName();
			boolean disjoint = false;
			boolean complete = false;
			String label = node.getLabel();
			Matcher matchMult = pDisjoint.matcher(label);
			if(matchMult.matches()){
				disjoint = true;
			}
			matchMult = pComplete.matcher(label);
			if(matchMult.matches()){
				complete = true;
			}
			UMLGeneralization umlGen = new UMLGeneralization(node.getName(),fatherName,disjoint,complete);
			ArrayList<NormalEdge> childrenNodes = node.getNormalEdges();
			for(int i = 0; i < childrenNodes.size(); i++){
				Node child = childrenNodes.get(i).getSource();
				if(child.equals(node)){
					child = childrenNodes.get(i).getTarget();
				}
				umlGen.addChild(child.getName());
			}

			writeFormulae(umlGen.translateFOL());
			writeFormulae(umlGen.translateDL());
		}
	}
	public void parseUMLKeyConstraint() throws SAXException,ParserConfigurationException{
		Iterator<EllipseNode> iter = this.ellipseNodes.values().iterator();
		while (iter.hasNext()){
			EllipseNode node = iter.next();
			UMLKeyConstraint constr = new UMLKeyConstraint();
			ArchEdge courrentEdge = node.getArchEdges().get(0);
			DiamondNode courrentNode = (DiamondNode) nextNode(node,courrentEdge);
			constr.addNameAssociation(courrentNode.getName());
			courrentEdge = nextEdge(courrentEdge,courrentNode);
			
			DiamondNode secondNode = (DiamondNode) nextNode(courrentNode,courrentEdge);
			constr.setNameCommonClass(getCommonClass(courrentNode,secondNode));
			
			while(courrentEdge != null){
				courrentNode = (DiamondNode) nextNode(courrentNode,courrentEdge);
				constr.addNameAssociation(courrentNode.getName());
				courrentEdge = nextEdge(courrentEdge,courrentNode);
			}
			writeFormulae(constr.translateFOL());
			writeFormulae(constr.translateDL());
		}
	}
	private String getCommonClass(DiamondNode n1,DiamondNode n2){
		UMLClassNode dn1 = (UMLClassNode) nextNode(n1,n1.getNormalEdges().get(0));
		UMLClassNode dn2 = (UMLClassNode) nextNode(n1,n1.getNormalEdges().get(1));
		UMLClassNode dn3 = (UMLClassNode) nextNode(n2,n2.getNormalEdges().get(0));
		UMLClassNode dn4 = (UMLClassNode) nextNode(n2,n2.getNormalEdges().get(1));
		if (dn1 == dn2 || dn1 == dn3 || dn1 == dn4){
			return dn1.getName();
		}
		else if (dn2 == dn3 || dn2 == dn4){
			return dn2.getName();
		}
		else if (dn3 == dn4){
			return dn3.getName();
		}
		return null;	
	}
	
	private String getLabel(Element el,String type){
		String[] strings = getLabels(el, type);
		for(int i = 0; i < strings.length; i++){
			if(!strings[i].equals(""))
				return strings[i];
		}
		return "";
	}
	private Node nextNode(Node oldNode,Edge edge){
		return edge.getSource().equals(oldNode) ? edge.getTarget() : edge.getSource();
	}
	private ArchEdge nextEdge(ArchEdge oldEdge, DiamondNode node){
		if (node.getArchEdges().size() == 1)
			return null;
		return node.getArchEdges().get(0).getSource() != node ? 
				node.getArchEdges().get(0) :
					node.getArchEdges().get(1);
	}
	private String[] getLabels(Element el,String type){
		NodeList nodes = el.getElementsByTagName(type);
		String[] labels = new String[nodes.getLength()];
		for (int i = 0; i < nodes.getLength(); i++){
			labels[i] = nodes.item(i).getTextContent();
		}
		return labels;
	}

	private void writeFormulae(ArrayList<String> str){
		if(str == null)
			return;
		for(int k = 0; k < str.size(); k++)
			this.formulaePrintStream.println(str.get(k));
	}

	private UMLAttribute parseUMLAttribute(String attribute, UMLClass umlClass){
		Matcher m = pAttribute.matcher(attribute);
		if (!m.matches()){
			return null;
		}
		return new UMLAttribute(m.group(1), m.group(2), m.group(4), m.group(5), umlClass);
	}
	private  UMLOperation parseUMLOperation(String operation, UMLClass umlClass){
		String[] strings = operation.split(operationSplit);
		Matcher matchName = pOperationName.matcher(strings[0]);
		if (!matchName.matches()){
			return null;
		}
		Matcher matchDomain = pOperationDomain.matcher(strings[strings.length-1]);
		if (!matchDomain.matches()){
			return null;
		}
		UMLOperation umlOperation = new UMLOperation(matchName.group(1), matchDomain.group(1), umlClass);
		if(!strings[2].equals("")){
			for (int i=1; i < strings.length-1; i++){
				Matcher matchAttrib = pOperationAttrib.matcher(strings[i]);
				if (!matchAttrib.matches()){
					return null;
				}
				umlOperation.addParameter(matchAttrib.group(1), matchAttrib.group(2));
			}
		}
		return umlOperation;
	}
	
	public static void main(String[] args){

		if (args.length == 2)
			new Parser(args[0],args[1]);
		else if (args.length == 1)
			new Parser(args[0],null);
		else
			System.out.println("You should insert as first parameter the graphml file.\n If you want to save the output in a file than insert it as second parameter, otherwise the output will be printed on standard output.");	
	}
}
