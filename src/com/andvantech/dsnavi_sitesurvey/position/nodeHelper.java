package com.andvantech.dsnavi_sitesurvey.position;

import java.util.ArrayList;
import java.util.Stack;

import com.andvantech.dsnavi_sitesurvey.position.graph.BiGraph;
import com.andvantech.dsnavi_sitesurvey.position.graph.GraphAlgorithms;
import com.andvantech.dsnavi_sitesurvey.position.graph.Node;


import android.util.Log;



public class nodeHelper{
	public BiGraph g;
	public Node currentNode;
	//public Node lastNode;
	public ArrayList<Node> predictionPath;
	ArrayList<Node> branchNodes;
	int branchCount;
	ArrayList<Integer> tmpBranch;
	Node tmpLastNode;
	ArrayList<Node> tracedNode;
	Node startNode;
	boolean isStartTrace;
	boolean isFoundPath;
	public Node node0;
	public Node node1;
	public Node node2;
	public Node node3;
	public Node node4;
	public Node node5;
	public Node node6;
	public Node node7;
	public Node node8;
	public Node node9;
	public Node node10;
	public Node node11;
	public Node node12;
	public Node node13;
	public Node node14;
	public Node node15;
	public Node node16;
	public Node node17;
	public Node node18;
	public Node node19;
	public Node node20;
	public Node node21;
	public Node node22;
	public Node node23;
	public Node node24;
	public Node node25;
	public Node node26;
	public Node node27;

	  public void initGuideNode(){
		  g = new BiGraph();
		  
		  node0=g.newNode("0");
		  node1=g.newNode("1");
		  node2=g.newNode("2");
		  node3=g.newNode("3");
		  node4=g.newNode("4");
		  node5=g.newNode("5");
		  node6=g.newNode("6");
		  node7=g.newNode("7");
		  node8=g.newNode("8");
		  node9=g.newNode("9");
		  node10=g.newNode("10");
		  node11=g.newNode("11");
		  node12=g.newNode("12");
		  node13=g.newNode("13");
		  node14=g.newNode("14");
		  node15=g.newNode("15");
		  node16=g.newNode("16");
		  node17=g.newNode("17");
		  node18=g.newNode("18");
		  node19=g.newNode("19");
		  node20=g.newNode("20");
		  node21=g.newNode("21");
		  node22=g.newNode("22");
		  node23=g.newNode("23");
		  //null node
		  node24=g.newNode("24");
		  node25=g.newNode("25");
		  node26=g.newNode("26");
		  node27=g.newNode("27");
		  
		  g.newEdge(node0, node1, "node0-node1");
		  g.newEdge(node1, node2, "node1-node2");
		  g.newEdge(node2, node3, "node2-node3");
		  g.newEdge(node3, node4, "node3-node4");
		  g.newEdge(node1, node5, "node1-node5");
		  g.newEdge(node5, node6, "node5-node6");
		  g.newEdge(node6, node24, "node6-node24");
		  g.newEdge(node7, node24, "node7-node24");
		  g.newEdge(node7, node8, "node7-node8");
		  g.newEdge(node8, node13, "node8-node13");
		  g.newEdge(node9, node24, "node9-node24");
		  g.newEdge(node9, node10, "node9-node10");
		  g.newEdge(node10, node11, "node10-node11");
		  g.newEdge(node11, node25, "node11-node25");
		  //g.newEdge(node11, node12, "node11-node12");
		  g.newEdge(node12, node13, "node12-node13");

		  g.newEdge(node14, node26, "node14-node26");
		  g.newEdge(node15, node26, "node15-node26");
		  g.newEdge(node16, node26, "node16-node26");
		  g.newEdge(node17, node26, "node17-node26");
		  
		  g.newEdge(node17, node18, "node17-node18");
		  g.newEdge(node18, node19, "node18-node19");
		  //g.newEdge(node19, node23, "node19-node23");
		  g.newEdge(node14, node20, "node14-node20");
		  g.newEdge(node20, node21, "node20-node21");
		  g.newEdge(node21, node22, "node21-node22");
		  g.newEdge(node22, node23, "node22-node23");

	  }

	  public void initVariable(){
		  this.branchCount = 0;
		  this.tmpBranch = new ArrayList<Integer>();
		  this.tmpBranch.clear();
		  //this.tmpBranch.add(0);
		  this.isFoundPath=false;
		  this.isStartTrace=false;
		  this.tmpLastNode = new Node();
		  this.predictionPath = new ArrayList<Node>();
		  this.branchNodes = new ArrayList<Node>();
		  this.tracedNode = new ArrayList<Node>();
		  this.startNode = new Node();
	  }
	  public ArrayList<Node> nextNodeLists(Node node){
		  ArrayList<Node> adjcentNode = this.g.adjacentNode(node);
		  ArrayList<Node> nextNodeList = new ArrayList<Node>();
		  for(int i=0;i<adjcentNode.size();i++){
			  if(!tracedNode.contains(adjcentNode.get(i))){
				  nextNodeList.add(adjcentNode.get(i));
			  }
		  }
		  return nextNodeList;
	  }
	  public void setCurrentNode(Node node){
		  this.currentNode = node;
	  }
	  public Node getCurrentNode(){
		  return this.currentNode;
	  }
	  /*public void setLastNode(Node node){
		  this.lastNode = node;
	  }
	  public Node getLastNode(){
		  return this.lastNode;
	  }*/
	  public void findPath(Node sensedNode, Node node){
		  ArrayList<Node> nextNode = nextNodeLists(node);
		  System.out.println(node.getID()+" branch size: "+nextNode.size());
		  System.out.println("branchCount "+branchCount);
		  System.out.println("tmpBranch  "+tmpBranch);
		  System.out.println("sensedNode  "+sensedNode.getID());
		  System.out.println("currentNode  "+node.getID());
		  
		  /*for(int i=0;i<this.predictionPath.size();i++){
			  System.out.print(this.predictionPath.get(i).getID()+"->");
		  }
		  System.out.println();*/
		  //System.out.println("isFoundPath "+isFoundPath);
		  //System.out.println("sensed node "+sensedNode.getID());
		  //System.out.println("current node "+this.currentNode.getID());
		  if(!node.equals(sensedNode)&&!isFoundPath){
			  if(!isStartTrace){
				  isStartTrace=true;
				  startNode = node;
				  predictionPath.add(nextNode.get(0));
				  branchCount++;
				  tracedNode.add(nextNode.get(0));
				  if(this.g.adjacentNode(node).size()==1)
					  tracedNode.add(node);
				  tmpBranch.add(branchCount);
				  findPath(sensedNode,nextNode.get(0));  
			  }else{
				  //tmpBranch.add(branchCount);
				  if(nextNode.size()==0){
					  //tmpBranch.add(branchCount);
					  int tmpBranch = this.tmpBranch.get(this.tmpBranch.size()-1);
					  for(int i=0;i<branchCount-tmpBranch;i++){
						  tracedNode.add(predictionPath.get(predictionPath.size()-1));
						  predictionPath.remove(predictionPath.size()-1);
					  }
					  branchCount=this.tmpBranch.get(this.tmpBranch.size()-1);
					  //this.tmpBranch.remove(this.tmpBranch.size()-1);
					  //if(this.tmpBranch.size()==0){
						//  branchCount=0;
						//  predictionPath.remove(predictionPath.size()-1);
					  //}
					  
				  }else if(nextNode.size()>0){
					  /*if(nextNode.contains(startNode)){
						  tracedNode.add(node);
						  findPath(sensedNode,startNode);
					  }*/
					  if(nextNode.size()>1)
						  tmpBranch.add(branchCount);  	  
					  for(int i=0;i<nextNode.size();i++){
						  if(!isFoundPath){
							  if(!nextNode.get(i).equals(startNode)){  
								  tracedNode.add(nextNode.get(i));
								  predictionPath.add(nextNode.get(i));
								  branchCount++;
								  //tmpBranch.add(branchCount);
								  findPath(sensedNode,nextNode.get(i));
							  }else{
								  tracedNode.add(nextNode.get(i));
								  predictionPath.clear();
								  branchCount=0;
								  tmpBranch.clear();
								  findPath(sensedNode,startNode);
							  }
						  }
					  } 
				  }
			  }
		  }else if(node.equals(sensedNode)&&!isFoundPath){
			  System.out.println("end path");
			  if(predictionPath.size()==0){
				  predictionPath.add(node);
			  }
			  for(int i=0;i<this.predictionPath.size();i++){
				  System.out.print(this.predictionPath.get(i).getID()+"->");
			  }
			  System.out.println();
			  isFoundPath=true;
			  this.tmpBranch.clear();
			  this.tracedNode.clear();
			  //tmpBranch=0;
			  //branchCount=0;
			  return;
		  }/*else if(lastNode.equals(sensedNode)&&!isFoundPath){
			  predictionPath.add(lastNode);
		  }*/
	  }
	  public void findPath2(Node sensedNode, Node node){//trace path with BFS
		  ArrayList<Node> path=GraphAlgorithms.path2(g, node, sensedNode);
		  for(int i=0;i<path.size();i++){
			  Log.v("test path2", path.get(i).getID());
			  predictionPath.add(path.get(i));
		  }
	  }
	  public ArrayList<Node> getPredictionPath(){
		  return predictionPath;
	  }
}

