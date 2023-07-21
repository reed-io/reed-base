/**
 * base/org.reed.struct/Tree.java
 */
package org.reed.struct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenxiwen
 * @date 2017年10月24日上午9:51:31
 */
public class Tree<E> {
    private Node<E> root;
    
    public Tree(Node<E> root){
        this.root = root;
    }
    
    public int getDepth(){
        return getDepth(root, 1);
    }
    
    private int getDepth(Node node, int level){
        if(node == null){
            return 0;
        }else if(node.getChildrenNum() == 0){
            return level++;
        }else{
            level++;
            int tmp = 0;
            for(Object o : node.getChildren()){
                if(o instanceof Node){
                    Node n = (Node)o;
                    int depth = getDepth(n, level);
                    tmp = depth>=tmp?depth:tmp;
                }
            }
            return tmp;
        }
    }

    /**
     * @return the root
     */
    public Node<E> getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(Node<E> root) {
        this.root = root;
    }
}

class Node<E>{
    private E data;
    private Node parent;
    private Set<Node> children;
    
    public Node(){
        this.data = null;
        this.parent = null;
        this.children = new HashSet<Node>(); 
    }
    
    public Node(E data){
        this.data = data;
        this.parent = null;
        this.children = new HashSet<Node>();
    }
//    public Node(E data, Node parent){
//        this.data = data;
//        this.parent = parent;
//        this.children = new HashSet<Node>();
//    }
    
    public void addChild(Node child){
        child.setParent(this);
        this.getChildren().add(child);
//        return this.getChildren().size();
    }
    
    public void addChildren(Node... children){
        for(Node child : children){
            addChild(child);
        }
    }
    
    public int getChildrenNum(){
        return this.children.size();
     }
    
    /**
     * @return the data
     */
    public E getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(E data) {
        this.data = data;
    }
    /**
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }
    /**
     * @param parent the parent to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }
    /**
     * @return the children
     */
    public Set<Node> getChildren() {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(Set<Node> children) {
        this.children = children;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node){
            Node n = (Node)obj;
            return this.data.equals(n.getData());
        }else{
            return false;
        }
    }
    
}